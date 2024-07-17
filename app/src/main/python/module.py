import librosa
import numpy as np
from fastdtw import fastdtw
import noisereduce as nr
import os
from os.path import dirname, join
from scipy.spatial.distance import cosine

def remove_silence(y):
    parts = librosa.effects.split(y, top_db=25, frame_length=1024, hop_length=512)
    # y_non_silent = []
    # for start, end in parts:
    #     y_non_silent.extend(y[start:end])
    #
    # y = np.array(y_non_silent)
    # return y
    y_non_silent = [y[start:end] for start, end in parts]
    return np.concatenate(y_non_silent)

def preprocessing(audio):
    y , sr = librosa.load(audio, sr=44100)
    y = librosa.effects.preemphasis(y)
    y = y/np.max(np.abs(y))
    y = remove_silence(y)
    y = nr.reduce_noise(y, sr, prop_decrease=0.8)
    return y,sr

def extraction(audio):
    y , sr = preprocessing(audio)
    mfccs = librosa.feature.mfcc(y=y,
                                 sr=sr,
                                 n_mfcc=13,
                                 window='hamming',
                                 win_length=1024,
                                 htk=True,
                                 hop_length=512,
                                 n_fft=1024,
                                 norm=None,
                                 dct_type=2,
                                 n_mels=20)
    return mfccs.T

def calculate_dtw(mfccs1, mfccs2):
    distance, _ = fastdtw(mfccs1, mfccs2, dist=cosine)
    return distance

def scoring(file_input, folder_template, template_score):
    audio_input = file_input
    templates = os.listdir(folder_template)
    total_distances = 0

    for template in templates:
        audio_template = os.path.join(folder_template, template)

        mfccs1 = extraction(audio_input)
        mfccs2 = np.loadtxt(audio_template, delimiter=',')

        dtw_distance = calculate_dtw(mfccs1, mfccs2)

        total_distances += dtw_distance

    average_distance = total_distances / len(templates)
    dif_distance = average_distance - template_score

    if dif_distance <= 0:
        final_score = 100
    else:
        final_score = int(100 - ((dif_distance / template_score) * 100))

    if final_score <=0:
        final_score = 0

    return final_score


def main(file_input, index_input):
    root = join(dirname(__file__), "Dataset_MFCC/")

    list_reference_folders = [
        ("01.Ha/", 10),
        ("02.Kha/", 10.5),
        ("03.Shad/", 15.5),
        ("04.Dhad/", 14.5),
        ("05.Tha/", 13.5),
        ("06.Dhza/", 17),
        ("07.AIn/", 15),
        ("08.Ghain/", 14),
        ("09.Qaf/", 13),
        ("10.He/", 14)
    ]
    dirPath_reference, score_reference = list_reference_folders[index_input]
    folder_reference = os.path.join(root, dirPath_reference)

    skor = scoring(file_input, folder_reference, score_reference)
    return skor
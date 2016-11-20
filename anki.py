#! /usr/bin/env python

import os
import itertools

src_dir = 'src/reasoned'

filenames = os.listdir(src_dir)

def process_segment(segment):
    try:
        i = (i for i,s in enumerate(segment) if s.startswith(';; <')).next()
        question = segment[0:i]
        answer = segment[i:]
    except StopIteration:
        question = segment
        answer = segment
    return {'question': question, 'answer': answer}

for filename in filenames[0:1]:
    with open(os.path.join(src_dir, filename), 'r') as f:
        segments = []
        for is_empty, group in itertools.groupby(f.readlines(), lambda s: len(s.strip()) == 0):
            if not is_empty:
                segments.append(list(group))

        segments.pop(0) # pop the ns declaration
        segments = map(process_segment, segments)
        print segments

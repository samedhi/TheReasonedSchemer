#! /usr/bin/env python

import os
import itertools

src_dir = 'src/reasoned'

filenames = os.listdir(src_dir)

def parser(segments):
    xs = []
    for is_comment, group in itertools.groupby(segments, lambda s: s.startswith(';;')):
        if is_comment:
            gs = map(lambda s: s[3:].strip(), group) # take ';;<' and ';;>' off, remove extra spaces
            s = ' '.join(gs) # combine multiline comments into one string
            xs.append({'text': s})
        else:
            xs.append({'code': ''.join(group)})
    return xs

def process_segment(segment):
    try:
        i = (i for i,s in enumerate(segment) if s.startswith(';;<')).next()
        return {'question': parser(segment[0:i]), 'answer': parser(segment[i:])}
    except StopIteration:
        return {'expression': parser(segment)}

for filename in filenames[0:1]:
    with open(os.path.join(src_dir, filename), 'r') as f:
        segments = []
        # This for loop splits code into segments, dividing using 1 or more blank lines
        for is_empty, group in itertools.groupby(f.readlines(), lambda s: len(s.strip()) == 0):
            if not is_empty:
                segments.append(list(group))

        segments.pop(0) # pop the ns declaration
        segments = map(process_segment, segments)
        print segments

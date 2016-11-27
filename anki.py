#! /usr/bin/env python
import itertools
import os
import subprocess

src_dir = 'src/reasoned'

filenames = os.listdir(src_dir)

def parser(segments):
    xs = []
    for is_comment, group in itertools.groupby(segments, lambda s: s.startswith(';;')):
        if is_comment:
            gs = map(lambda s: s[3:].strip(), group) # take ';;<' and ';;>' off, remove extra spaces
            s = ' '.join(gs) + '\n' # combine multiline comments into one string
            xs.append({'type': 'text', 'value': s})
        else:
            xs.append({'type': 'code', 'value': ' '.join(group)})
    return xs

def process_segment(segment):
    try:
        # The following throws StopIteration if no line contains a answer (';;<')
        i = (i for i,s in enumerate(segment) if s.startswith(';;<')).next()
        return {'question': parser(segment[0:i]), 'answer': parser(segment[i:])}
    except StopIteration:
        return {'question': parser(segment)}

def eval_segment(segment, accumulator):
    for k,vs in segment.items():
        assert k in ['question','answer']
        for section in vs:
            assert section['type'] in ['code', 'text']
            if 'code' in section['type']:
                accumulator.append(section['value'])
                s = ' '.join(accumulator)
                CMD = ['boot', 'load-code', '-c', '%s' % s]
                try:
                    section['result'] = subprocess.check_output(CMD)
                except subprocess.CalledProcessError:
                    print "===== CODE ======"
                    print s
                    print "===== CMD ======="
                    print ' '.join(CMD)
                    raise

for filename in filenames[0:1]:
    with open(os.path.join(src_dir, filename), 'r') as f:
        segments = []
        accumulator = []
        cards = ['\t']

        # This for loop splits code into segments, dividing segments by 1 or more blank lines
        for is_empty, group in itertools.groupby(f.readlines(), lambda s: len(s.strip()) == 0):
            if not is_empty:
                segments.append(list(group))

        accumulator.append(''.join(segments.pop(0))) # add ns declaration to begining of accumulator

        for i,segment in enumerate(segments):
            s1 = process_segment(segment)
            segments[i] = s1
            eval_segment(s1, accumulator)

        # give an answer to all the segments that end on a code block
        for segment in segments:
            if 'answer' not in segment:
                v = segment['question'][-1].pop('result')
                segment['answer'] = [{'type': 'text', 'value': v}]

        # transforms all "code" into "text"
        for segment in segments:
            for k,vs in segment.items():
                for i,v in enumerate(vs):
                    if v['type'] == 'code':
                        if 'result' in v:
                            s = '%s=> %s' % (v['value'], v['result'])
                        else:
                            s = v['value']
                        vs[i] = {'type': 'text', 'value': s}

        for segment in segments:
            print segment
            ks = ['question', 'answer']
            q, a = [' '.join( map(lambda d: d['value'], segment[k]) ) for k in ks]
            s = "%s <<>> %s" % (q, a)
            s = s.replace('\n', '<br>') # Anki needs \n to specify csv
            cards.append(s)

        print '\n'.join(cards)

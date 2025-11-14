#!/bin/bash
bash compile.sh
bash expand.sh
java VirtualMemorySimulation –s 512 –b 16 –a 4 –r rr –p 1024 –n 100 –u 75 -f Trace1.trc -f Trace2_4Evaluation.trc –f Corruption1.trc

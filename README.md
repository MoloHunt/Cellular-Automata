# Cellular-Automata
Small Java program exploring Cellular automata and smoothing to randomly generate images.

To Use you start up the program and the screen will load with a random, unsmoothed "cell map"

By pressing space you can set play to true and the program will run through the smoothing algorithm, which involves counting the surrounding cells and changing itself based on that. The first iteration is very drastic and the subsequent ones not so much. By default the program runs with a fill percentage (used to determine the initial cell coverage) of 50% and runs the smoothign algorithm 3 times. By pressing 'C' you can start the options sequence, here you can set a custom fill percent and set a custom number of smoothing iterations

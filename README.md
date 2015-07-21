# Markov Chain Text Generator
Java program to produce random text using Markov Chains.
Includes a basic GUI made using JavaFX.

## Details
- Made using Java 8 (not tested on other versions)
    - Uses [Google's Guava][https://github.com/google/guava] library
- Uses Python script to gather comments from Reddit to generate markov chain model
    - Tested using Python 3
    - Requires PRAW library
- Basic text generation so output might not be the best
- Can generate a model based on two methods
    1. A local .txt file on the users system
    2. (Initial/Beta) Reddit comments from a subreddit of the user's choosing (default: /r/programming)

## Todo:
- Try and make the generator end its sentences
    - Now it will just print words until it runs out of words to generate, doesn't always end up with coherent sentences
- Add new UI elements to let the user control:
    1. Length of generated text

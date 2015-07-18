__author__ = 'ndigati'

import praw
import re
import sys

r = praw.Reddit('Reddit comment grabber by /u/Crazy_duck28')


def get_comments(subreddit='programming', num=5):
    print(subreddit)
    comments = []
    subreddit = r.get_subreddit(subreddit)
    for submission in subreddit.get_hot(limit=num):
        # only want to grab 100 comments at most
        if len(comments) >= 100:
            return comments

        post_comments = submission.comments
        for comment in post_comments:
            try:
                if comment.is_root:
                    comments.append(comment.body)
            except AttributeError:
                pass
    return comments


def export_to_file(comments, filename):
    with open(filename, "a+") as file:
        file.seek(0)
        file.truncate()

        for comment in comments:
            # Regular expression to find all the comments that have links
            # Remove those links from the comment
            comment = re.sub(r'(https?://.*[\r\n]*)', '', comment)

            # Remove extra formatting from reddit comments
            comment = comment.replace('*', '').replace('^', '').replace('-', '').replace('>', '')

            # Remove excess whitespace from the comment
            comment = ' '.join(comment.split())

            # Remove usernames from comments
            comment = re.sub(r'(\[/u/.+\])|(/u/.+ )', '', comment)

            # Enclose comment in ' {{ ' and ' }} ' to separate comments in the resulting file
            file.write("{{ " + comment + " }} ")

if __name__ == "__main__":
    comments = []
    if len(sys.argv) < 2:
        comments = get_comments()
    elif len(sys.argv) > 2:
        print("Too many command line arguments!")
        sys.exit(0)
    else:
        comments = get_comments(sys.argv[1])
    export_to_file(comments, "./comments.txt")

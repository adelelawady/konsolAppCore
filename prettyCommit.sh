#!/bin/bash

# Prompt the user for the commit message
echo "Enter your commit message: "
read commit_message

# Ensure that a commit message is provided
if [ -z "$commit_message" ]; then
  echo "Commit message cannot be empty"
  exit 1
fi

# Get a random emoji from the emojihub API
emoji=$(curl -s "https://emojihub.herokuapp.com/api/random" | grep -o '"htmlCode":\["[^"]*' | sed 's/"htmlCode":\["//')

# Combine the commit message with the random emoji
commit_message="$emoji $commit_message"

# Stage all files
git add .

# Commit with the message
git commit -m "$commit_message"

echo "Committed with message: $commit_message"

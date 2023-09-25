#!/bin/bash

# Check if the "-v" argument is present
if [ "$1" == "-v" ]; then
  VERBOSE=true
  shift # Remove the "-v" argument
else
  VERBOSE=false
fi

# Check if the XML file path is provided as an argument
if [ -z "$1" ]; then
  echo "Usage: $0 [-v] /env/app/config.xml"
  exit 1
fi

# Run the Java program
java env.RedisDataExporter "$@" # Pass all remaining arguments to the Java program

# Print all keys saved to Redis if VERBOSE is true
if [ "$VERBOSE" = true ]; then
  redis-cli keys '*'
fi

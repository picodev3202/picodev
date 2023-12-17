#!/usr/bin/env sh
#
#
#
#
#
#
#
#
#

###

CMD='nvim --clean -l'
SCRIPT="$(dirname "$(realpath "$0")")"/lua/app.lua

RESULT_CMD=$($CMD "$SCRIPT" debug)

echo "RESULT_CMD: })>$RESULT_CMD<({"
sh -c "$RESULT_CMD $*"

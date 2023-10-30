#!/usr/bin/env sh

exitWithErrorMsg() {
  if [ "" = "$1" ]; then
    echo "Unknown Error"
    exit 101
  fi
  echo "$*"
  exit "$1"
} >&2

warningMsg() {
  echo "$*"
} >&2

ensureRunDir() {
  UID=$(id --user)
  RUN_DIR=/var/run/user/$UID
  if [ ! -d "$RUN_DIR" ]; then
    exitWithErrorMsg 103 "runtime dir $RUN_DIR is not exist"
  fi
}

lookupOneRunCmd() {
  ensureRunDir
  TOOL_ONE_RUN_CMD=/opt/local/gradle/bin/gradle
  TOOL_ONE_RUN_HOME="$RUN_DIR/one_run_home"
  TOOL_ONE_RUN_DEFAULT_ARGS="--gradle-user-home=$TOOL_ONE_RUN_HOME --offline"
  export JAVA_HOME=/opt/local/jdk
}

ensureOneRunCmd() {
  lookupOneRunCmd # and ensureRunDir()
  if [ ! -e "$TOOL_ONE_RUN_CMD" ]; then
    exitWithErrorMsg 153 "one_run_cmd $TOOL_ONE_RUN_CMD is not exist"
  fi

  mkdir --parents "$TOOL_ONE_RUN_HOME"
  touch "$TOOL_ONE_RUN_HOME"/file_from_app_by_script

  if [ -d /opt/local/generated-jars ]; then
    VERSION=$(cat /opt/local/generated-jars/version)
    if [ ! -d "$TOOL_ONE_RUN_HOME/caches/$VERSION/generated-gradle-jars/" ]; then
      echo "copy generated-jars $VERSION"
      mkdir --parents "$TOOL_ONE_RUN_HOME/caches/$VERSION/generated-gradle-jars/"
      cp /opt/local/generated-jars/*.jar "$TOOL_ONE_RUN_HOME/caches/$VERSION/generated-gradle-jars/"
    fi
  fi
}

lookupDevPlace() { # lookupDevPlace $SCRIPT_FULL_PATH
  SCRIPT_FULL_PATH="$1"
  SCRIPT_NAME="$(basename "$SCRIPT_FULL_PATH")"
  PLACE_OF_SCRIPT_FULL_PATH="$(dirname "$SCRIPT_FULL_PATH")"
  DEV_PLACE_ROOT_FULL_PATH="$(realpath "$PLACE_OF_SCRIPT_FULL_PATH/../../..")"
  DEV_PLACE_NAME=$(head -n1 "$DEV_PLACE_ROOT_FULL_PATH/.internal/place_config_desc")
  DEV_PLACE_GEN_DIR="$DEV_PLACE_ROOT_FULL_PATH/wwgen"
  DEV_PLACE_GEN_TMP="$DEV_PLACE_GEN_DIR/tmp"
}

ensure_app_by_script_place() { # lookupDevPlace $SCRIPT_FULL_PATH
  SCRIPT_FULL_PATH="$1"
  lookupDevPlace "$SCRIPT_FULL_PATH"

  #  echo "SCRIPT_FULL_PATH          $SCRIPT_FULL_PATH"
  #  echo "PLACE_OF_SCRIPT_FULL_PATH $PLACE_OF_SCRIPT_FULL_PATH"
  #  echo "SCRIPT_NAME               $SCRIPT_NAME"
  #  echo "DEV_PLACE_ROOT_FULL_PATH  $DEV_PLACE_ROOT_FULL_PATH"
  #  echo "DEV_PLACE_NAME            $DEV_PLACE_NAME"

  ensureRunDir

  RUN_PLACE_APP__TMP="$RUN_DIR/orun_$DEV_PLACE_NAME"

  APP_BY_SCRIPT_NAME="$(basename "$(realpath "$PLACE_OF_SCRIPT_FULL_PATH/..")")"
  APP_BY_SCRIPT__DIR="$RUN_PLACE_APP__TMP/$APP_BY_SCRIPT_NAME"
  APP_BY_SCRIPT__DIR_LINK="$DEV_PLACE_GEN_TMP/run_$APP_BY_SCRIPT_NAME"
  APP_BY_SCRIPT_TOOL_FILE="$APP_BY_SCRIPT__DIR/build.gradle.kts"

  # TMP_FILE="$APP_BY_SCRIPT__DIR/$(date +%d_%Hh%Mm%Ss%N)"

  if [ ! -d "$DEV_PLACE_GEN_TMP" ]; then
    echo "mk $DEV_PLACE_GEN_TMP"
    mkdir --parents "$DEV_PLACE_GEN_TMP"
  else
    echo "$DEV_PLACE_GEN_TMP exists"
  fi

  mkdir --parents "$APP_BY_SCRIPT__DIR"

  if [ -L "$APP_BY_SCRIPT__DIR_LINK" ]; then
    echo link "$APP_BY_SCRIPT__DIR_LINK" of "$APP_BY_SCRIPT__DIR" exists
  else
    echo link "$APP_BY_SCRIPT__DIR" to "$APP_BY_SCRIPT__DIR_LINK"
    ln -s "$APP_BY_SCRIPT__DIR" "$APP_BY_SCRIPT__DIR_LINK"
  fi

  touch "$RUN_PLACE_APP__TMP/file_from_app_by_script"
  touch "$APP_BY_SCRIPT__DIR/file_from_app_by_script"
  touch "$APP_BY_SCRIPT__DIR/settings.gradle.kts"
  #touch "$APP_BY_SCRIPT_TOOL_FILE"

  if [ -e "$APP_BY_SCRIPT__DIR_LINK/file_from_app_by_script" ]; then
    cd "$APP_BY_SCRIPT__DIR_LINK" || exitWithErrorMsg 121 "TODO: unable to cd $APP_BY_SCRIPT__DIR_LINK"
    echo "CWD  $(pwd)"

    ensureOneRunCmd

  else
    exitWithErrorMsg 120 "TODO: $APP_BY_SCRIPT__DIR        $APP_BY_SCRIPT__DIR_LINK not found"
  fi
}

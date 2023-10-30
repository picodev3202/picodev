----
----
----

local code = {}

function code.log_info_permanent(...)
    print(...)
    print("\n")
end

function code.log_info(...)
    print(...)
    print("\n")
end

function code.log_warn(...)
    print(...)
    print("\n")
end

function code.log_debug(...)
    print(...)
    print("\n")
end

function code.exitWithErrorMsg(exitCode, msg)
    if "" == "$1" then
        code.log_warn "Unknown Error"
        os.exit(101)
    end
    code.log_warn(msg) -- >&2
    os.exit(exitCode)
end

function code.warningMsg(msg)
    code.log_warn("warning msg :", msg) -- >&2
end

function code.shift_(n, args)
    local args_result = {}
    for i = 1 + n, #args do
        table.insert(args_result, args[i])
    end
    return args_result
end

function code.shift_one(args)
    return code.shift_(1, args)
end

function code.str_trim_end(str)
    return string.gsub(str, "%s+$", "")
end

function code.str_trim_start(str)
    return string.gsub(str, "^%s+", "")
end

function code.str_trim(str)
    local result = code.str_trim_start(str)
    return code.str_trim_end(result)
end

function code.str_join(delimiter, ...)
    local _delimiter = ""
    local result = ""
    for _, v in pairs({ ... }) do
        result = result .. _delimiter .. v
        _delimiter = delimiter
    end
    return result
end

function code.path_join(...)
    return vim.fs.normalize(code.str_join("/", ...))
end

function code.read_process_output(cmd)
    local process = assert(io.popen(cmd, 'r'))
    local processOut = assert(process:read('*a'))
    -- local exitCode =
    process:close()
    return processOut
end

function code.path_real(...)
    local path = code.path_join(...)
    return code.str_trim_end(code.read_process_output("test -e " .. path .. " && realpath " .. path .. " || echo -n " .. path))
end

function code.mkdir(path)
    return vim.fn.mkdir(path)
end

function code.mkdirs(path)
    return vim.fn.mkdir(path, "p")
end

function code.ensureRunDir ()
    code.UID = code.str_trim_end(code.read_process_output("id --user"))
    code.RUN_DIR = "/run/user/" .. code.UID
    if vim.fn.isdirectory(code.RUN_DIR) ~= 1 then
        code.exitWithErrorMsg(103, "runtime dir $RUN_DIR is not exist")
    end
end

function code.lookupOneRunCmd()
    code.ensureRunDir()
    code.TOOL_I_D_E = "/opt/local/idea/bin/idea.sh"
    code.JAVA_HOME = "/opt/local/jdk"
    code.TOOL_ONE_RUN_CMD = "/opt/local/gradle/bin/gradle"
    code.TOOL_ONE_RUN_HOME = code.path_real(code.RUN_DIR, "one_run_home")
    code.TOOL_ONE_RUN_DEFAULT_ARGS = "--gradle-user-home=" .. code.TOOL_ONE_RUN_HOME .. " --offline"
end

function code.ensureOneRunCmd()
    code.lookupOneRunCmd() -- # and ensureRunDir()
    if vim.fn.filereadable(code.TOOL_ONE_RUN_CMD) ~= 1 then
        code.exitWithErrorMsg(153, "one_run_cmd " .. code.TOOL_ONE_RUN_CMD .. " is not exist")
    end
    code.mkdirs(code.TOOL_ONE_RUN_HOME)
    os.execute("touch " .. code.TOOL_ONE_RUN_HOME .. "/file_from_app_by_scrip")
    local generatedJarsDir = "/opt/local/generated-jars"
    local generatedJarsVersionFile = code.path_join(generatedJarsDir, "version")
    if vim.fn.isdirectory(generatedJarsDir) == 1 then
        local version = vim.fn.readfile(generatedJarsVersionFile)[1]
        local toolOneRunHomeDir = code.path_join(code.TOOL_ONE_RUN_HOME, "caches", version, "generated-gradle-jars/")
        if vim.fn.isdirectory(toolOneRunHomeDir) ~= 1 then
            local copyCmd = "cp " .. generatedJarsDir .. "/*.jar " .. toolOneRunHomeDir
            code.log_info("copy generated-jars ", version, " by: })> " .. copyCmd .. " <({")
            code.mkdirs(toolOneRunHomeDir)
            os.execute(copyCmd)
        end
    else
        code.mkdir(generatedJarsDir)
        local appName = 'Gradle'
        local appVersion = code.read_process_output(code.getOneRunCmd() .. " --version | grep " .. appName)
        appVersion = appVersion:gsub(appName, "")
        appVersion = code.str_trim(appVersion)

        local toolOneRunHomeDir = code.path_join(code.TOOL_ONE_RUN_HOME, "caches", appVersion, "generated-gradle-jars/")
        vim.fn.writefile({ appVersion }, generatedJarsVersionFile)
        local copyCmd = "cp " .. toolOneRunHomeDir .. "/*.jar " .. generatedJarsDir
        code.log_info("copy generated-jars ", appVersion, " by: })> " .. copyCmd .. " <({")
        os.execute(copyCmd)
    end
end

function code.getOneRunCmd()
    code.lookupOneRunCmd()
    local full_cmd = ""
    full_cmd = "JAVA_HOME=" .. code.JAVA_HOME .. " "
    full_cmd = full_cmd .. code.TOOL_ONE_RUN_CMD .. " " .. code.TOOL_ONE_RUN_DEFAULT_ARGS
    return full_cmd
end

function code.echoOneRunCmd(args)
    code.ensureOneRunCmd()
    io.output():write(code.getOneRunCmd())
end

function code.echoArgs(args)
    io.output():write(vim.inspect(args), "\n")
end

function code.lookupDevPlace()
    code.PLACE_OF_SCRIPT_FULL_PATH = vim.fs.dirname(vim.fs.dirname(arg[0]))
    code.DEV_PLACE_ROOT_FULL_PATH = code.path_real(code.PLACE_OF_SCRIPT_FULL_PATH, "../../..")
    code.DEV_PLACE_NAME = vim.fn.readfile(code.DEV_PLACE_ROOT_FULL_PATH .. "/.internal/place_config_desc")[1]
    code.DEV_PLACE_GEN_DIR = code.path_join(code.DEV_PLACE_ROOT_FULL_PATH, "wwgen")
    -- code. DEV_PLACE_GEN_TMP = code.path_join(code.DEV_PLACE_GEN_DIR, "tmp")
    code.DEV_PLACE_GEN_LNK = code.path_join(code.DEV_PLACE_GEN_DIR, "lnk")
end

function code.ensure_app_by_script_place()
    local app_by_script_run_place_prefix = "orun_"
    code.lookupDevPlace()
    code.ensureRunDir()
    code.RUN_PLACE_APP__TMP = code.path_join(code.RUN_DIR, app_by_script_run_place_prefix .. code.DEV_PLACE_NAME)
    code.APP_BY_SCRIPT_NAME = vim.fs.basename(code.path_real(code.PLACE_OF_SCRIPT_FULL_PATH, ".."))
    code.APP_BY_SCRIPT__DIR = code.path_join(code.RUN_PLACE_APP__TMP, code.APP_BY_SCRIPT_NAME)
    code.APP_BY_SCRIPT__DIR_LINK = code.path_join(code.DEV_PLACE_GEN_LNK, "run_" .. code.APP_BY_SCRIPT_NAME)
    code.APP_BY_SCRIPT_TOOL_FILE = code.path_join(code.APP_BY_SCRIPT__DIR, "build.gradle.kts")

    if vim.fn.isdirectory(code.DEV_PLACE_GEN_LNK) ~= 1 then
        code.log_info("mk ", code.DEV_PLACE_GEN_LNK)
        code.mkdirs(code.DEV_PLACE_GEN_LNK)
    else
        code.log_info(code.DEV_PLACE_GEN_LNK, " exists")
    end
    code.mkdirs(code.APP_BY_SCRIPT__DIR)

    local fileMarker = "file_marker_from_app_by_script"
    local fileMarkerContent = code.DEV_PLACE_GEN_DIR
    vim.fn.writefile({ fileMarkerContent }, code.path_join(code.RUN_PLACE_APP__TMP, "file_from_app_by_script"))
    vim.fn.writefile({ fileMarkerContent }, code.path_join(code.APP_BY_SCRIPT__DIR, fileMarker))
    vim.fn.writefile({ "" }, code.path_join(code.APP_BY_SCRIPT__DIR, "settings.gradle.kts"))
    -- vim.fn.writefile({ "" }, code.APP_BY_SCRIPT_TOOL_FILE)
    local appDirLink = code.path_join(code.APP_BY_SCRIPT__DIR_LINK, fileMarker)
    code.log_info("appDirLink", appDirLink, "\n")
    if (vim.fn.filereadable(appDirLink) ~= 1) or (fileMarkerContent ~= vim.fn.readfile(appDirLink)[1]) then
        if (vim.fn.filereadable(appDirLink) == 1) and (fileMarkerContent ~= vim.fn.readfile(appDirLink)[1]) then
            os.execute("rm " .. code.APP_BY_SCRIPT__DIR_LINK)
        end
        local linkAppRunDirCmd = "ln -s " .. code.APP_BY_SCRIPT__DIR .. " " .. code.APP_BY_SCRIPT__DIR_LINK
        code.log_info("link by cmd:  })> " .. linkAppRunDirCmd .. " <({")
        os.execute(linkAppRunDirCmd)
    end
    code.ensureOneRunCmd()
end

function code.echoAppsByScriptStartCmd(args)
    code.ensure_app_by_script_place()
    local apps_list = {}
    for _, v in ipairs(args) do
        table.insert(apps_list, code.path_join(code.PLACE_OF_SCRIPT_FULL_PATH, v))
    end
    code.log_info("apps_list:", vim.inspect(apps_list), "\n")
    vim.fn.writefile(apps_list, code.path_join(code.APP_BY_SCRIPT__DIR, "apps_list.txt"))
    local scriptTemplateName = vim.fs.basename(code.PLACE_OF_SCRIPT_FULL_PATH)
    local scriptTemplateFile = code.path_join(code.PLACE_OF_SCRIPT_FULL_PATH, "../plus/scriptTemplate/kt", scriptTemplateName .. ".kt")
    vim.fn.writefile(code.shift_(10, vim.fn.readfile(scriptTemplateFile)), code.APP_BY_SCRIPT_TOOL_FILE)
    io.output():write("cd " .. code.APP_BY_SCRIPT__DIR_LINK .. " && ")
    code.echoOneRunCmd(args)
end

function code.link_out_place(args)
    code.lookupDevPlace()
    code.ensureRunDir()
    local RUN_OUT_DIR = code.path_join(code.RUN_DIR, "out_" .. code.DEV_PLACE_NAME)
    local fileOfMarker = code.path_join(RUN_OUT_DIR, "file_from_app_by_script")
    local PRJ_OUT = code.path_join(code.DEV_PLACE_GEN_LNK, "out")

    code.log_info_permanent("RUN_OUT_DIR    ", RUN_OUT_DIR)
    code.log_info_permanent("PRJ_OUT        ", PRJ_OUT)

    code.log_debug("PRJ_OUT=" .. PRJ_OUT .. " ;  if [ -L \"$PRJ_OUT\" ]; then  echo \"cleanup link $PRJ_OUT\" ; rm \"$PRJ_OUT\" ; fi")
    local cmd_rm_link = "if [ -L '" .. PRJ_OUT .. "' ]; then  echo 'cleanup link " .. PRJ_OUT .. "' >&2 ; rm '" .. PRJ_OUT .. "' ; fi"
    code.log_info("cleanup link " .. PRJ_OUT .. " by cmd:  })> " .. cmd_rm_link .. " <({")
    os.execute(cmd_rm_link)

    if vim.fn.isdirectory(PRJ_OUT) == 1 then
        local cmd = "rm -rf " .. PRJ_OUT
        code.log_info("cleanup dir " .. PRJ_OUT .. "by cmd:  })> " .. cmd .. " <({")
        os.execute(cmd)
    end

    if vim.fn.isdirectory(RUN_OUT_DIR) == 1 then
        if vim.fn.filereadable(fileOfMarker) == 1 then
            local cmd = "rm -rf " .. RUN_OUT_DIR
            code.log_info("cleanup run " .. RUN_OUT_DIR .. "by cmd:  })> " .. cmd .. " <({")
            os.execute(cmd)
        end
    end

    code.mkdirs(code.DEV_PLACE_GEN_LNK)
    code.mkdirs(RUN_OUT_DIR)
    vim.fn.writefile({ "" }, fileOfMarker)
    local cmd_out_link = "ln -s " .. RUN_OUT_DIR .. " " .. PRJ_OUT
    code.log_info("link by cmd:})>" .. cmd_out_link .. "<({")
    os.execute(cmd_out_link)
end

function code.open_in_ide(args)
    code.link_out_place(args)
    code.lookupOneRunCmd()
    io.output():write(code.TOOL_I_D_E .. " " .. code.path_join(code.DEV_PLACE_ROOT_FULL_PATH, code.DEV_PLACE_NAME))
end

local debug = {}

function debug.one(args)
    print(vim.inspect(args), "\n")
    print("SCRIPT                    ", arg[0])

    code.ensure_app_by_script_place()
    code.echoOneRunCmd(args)

    print("APP_BY_SCRIPT_NAME        ", code.APP_BY_SCRIPT_NAME)
    print("APP_BY_SCRIPT_TOOL_FILE   ", code.APP_BY_SCRIPT_TOOL_FILE)
    print("APP_BY_SCRIPT__DIR        ", code.APP_BY_SCRIPT__DIR)
    print("APP_BY_SCRIPT__DIR_LINK   ", code.APP_BY_SCRIPT__DIR_LINK)
    print("DEV_PLACE_GEN_DIR         ", code.DEV_PLACE_GEN_DIR)
    print("DEV_PLACE_GEN_TMP         ", code.DEV_PLACE_GEN_LNK)
    print("DEV_PLACE_NAME            ", code.DEV_PLACE_NAME)
    print("DEV_PLACE_ROOT_FULL_PATH  ", code.DEV_PLACE_ROOT_FULL_PATH)
    print("JAVA_HOME                 ", code.JAVA_HOME)
    print("PLACE_OF_SCRIPT_FULL_PATH ", code.PLACE_OF_SCRIPT_FULL_PATH)
    print("RUN_DIR                   ", code.RUN_DIR)
    print("RUN_PLACE_APP__TMP        ", code.RUN_PLACE_APP__TMP)
    print("TOOL_ONE_RUN_CMD          ", code.TOOL_ONE_RUN_CMD)
    print("TOOL_ONE_RUN_DEFAULT_ARGS ", code.TOOL_ONE_RUN_DEFAULT_ARGS)
    print("TOOL_ONE_RUN_HOME         ", code.TOOL_ONE_RUN_HOME)
    print("UID                       ", code.UID)
    print("\n")

    --[[
    ]]
end

--return code
function code.main()
    local actions = {
        ["debug"]                      --[[]] = debug.one,
        ["echoOneRunCmd"]              --[[]] = code.echoOneRunCmd,
        ["echoArgs"]                   --[[]] = code.echoArgs,
        ["echoAppsByScriptStartCmd"]   --[[]] = code.echoAppsByScriptStartCmd,
        ["link_out_place"]             --[[]] = code.link_out_place,
        ["open_in_ide"]                --[[]] = code.open_in_ide,
    }
    local actionName = arg[1]
    if nil == actionName then
        code.log_warn("action undefined, possible action is:")
        for k, v in pairs(actions) do
            code.log_warn("    " .. k)
        end
        os.exit(131)
    end
    local action = actions[actionName]
    code.log_debug("action is:", actionName)
    if nil == action then
        code.exitWithErrorMsg(132, "action by name '" .. actionName .. "' undefined")
    end
    action(code.shift_one(arg))
end

code.main()

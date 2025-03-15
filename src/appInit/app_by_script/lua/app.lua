----
----
----

local code = {}
code.ONE_RUN_CMD_SCRIPT = "build.gradle.kts"
code.ONE_RUN_CMD_SETTINGS = "settings.gradle.kts"

code.fs = {}

function code.log_info_permanent(...)
    print(...)
    --print("\n")
end

function code.log_info(...)
    print(...)
    --print("\n")
end

function code.log_warn(...)
    print(...)
    --print("\n")
end

function code.log_debug(...)
    --print(...)
    --print("\n")
end

function code.log_debug_lite(...)
    --print(...)
    --print("\n")
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

function code.isTrue(intValue)
    return intValue == 1
end

function code.fs.isLink(fsPath)
    local result = vim.uv.fs_lstat(fsPath)
    --code.log_info("isFsLink :", result)
    --code.log_info("isFsLink :", nil == result)
    if nil ~= result and result.type == "link" then
        return 1
    end
    return 0
end
function code.fs.isDirectory(fsPath)
    local result = vim.uv.fs_lstat(fsPath)
    --code.log_info("isFsDirectory :", result)
    --code.log_info("isFsDirectory :", nil == result)
    if nil ~= result and result.type == "directory" then
        return 1
    end
    return 0
end

function code.fs.rm_local_path(pathOfLocalFile)
    if code.isTrue(code.fs.isLink(pathOfLocalFile)) then
        vim.uv.fs_unlink(pathOfLocalFile)
    else
        if code.isTrue(code.fs.isDirectory(pathOfLocalFile)) then
            local cmd = "rm -rf " .. pathOfLocalFile
            code.log_debug_lite("cleanup run " .. pathOfLocalFile .. " by cmd:  })> " .. cmd .. " <({")
            os.execute(cmd)
        else
            vim.uv.fs_unlink(pathOfLocalFile)
        end
    end
end

function code.path_join(...)
    return vim.fs.normalize(code.str_join("/", ...))
end

function code.path_real(...)
    local path = code.path_join(...)
    return vim.uv.fs_realpath(path) -- https://neovim.io/doc/user/luvref.html#uv.fs_realpath()
end

--function code.mkdir(path)
--    return vim.fn.mkdir(path)
--end

function code.mkdirs(path)
    return vim.fn.mkdir(path, "p")
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

function code.read_process_output(cmd)
    local process = assert(io.popen(cmd, 'r'))
    local processOut = assert(process:read('*a'))
    -- local exitCode =
    process:close()
    return processOut
end

function code.ensureRunDir ()
    code.UID = code.str_trim_end(code.read_process_output("id --user"))
    code.runDir = vim.uv.fs_realpath("/var/run/user/" .. code.UID)
    if vim.fn.isdirectory(code.runDir) ~= 1 then
        code.exitWithErrorMsg(103, "runtime dir $RUN_DIR is not exist")
    end
end

function code.lookupOneRunCmd()
    code.ensureRunDir()
    code.TOOL__IDE = "/opt/local/tool/ide/bin/app"
    code.TOOL_IDE2 = "/opt/local/tool/ide2/bin/app"
    code.JAVA_HOME = "/opt/local/jdk"
    code.TOOL_ONE_RUN_CMD = "/opt/local/gradle/bin/gradle"
    code.TOOL_ONE_RUN_HOME = code.path_join(code.runDir, "_u_one_run_home")
    code.TOOL_ONE_RUN_DEFAULT_ARGS = "--gradle-user-home=" .. code.TOOL_ONE_RUN_HOME .. " --offline"
    -- code.TOOL_ONE_RUN_DEFAULT_ARGS = "--gradle-user-home=" .. code.TOOL_ONE_RUN_HOME .. " --offline --stacktrace"
end

function code.ensureOneRunCmd()
    code.lookupOneRunCmd() -- # and ensureRunDir()
    if vim.fn.filereadable(code.TOOL_ONE_RUN_CMD) ~= 1 then
        code.exitWithErrorMsg(153, "one_run_cmd " .. code.TOOL_ONE_RUN_CMD .. " is not exist")
    end
    code.mkdirs(code.TOOL_ONE_RUN_HOME)
    os.execute("touch " .. code.TOOL_ONE_RUN_HOME .. "/file_from_app_by_script")
    local gradleGeneratedDir = "/opt/local/gradle_generated/ready"
    local generatedJarsVersionFile = code.path_join(gradleGeneratedDir, "version")
    if vim.fn.isdirectory(gradleGeneratedDir) == 1 then
        local version = vim.fn.readfile(generatedJarsVersionFile)[1]
        local toolOneRunHomeDir = code.path_join(code.TOOL_ONE_RUN_HOME, "caches", version, "generated-gradle-jars/")
        if vim.fn.isdirectory(toolOneRunHomeDir) ~= 1 then
            local copyCmd = "cp " .. gradleGeneratedDir .. "/*.jar " .. toolOneRunHomeDir
            code.log_info("copy generated-jars ", version, " by: })> " .. copyCmd .. " <({")
            code.mkdirs(toolOneRunHomeDir)
            os.execute(copyCmd)
        end
    else
        local oneRunHomePlus = code.TOOL_ONE_RUN_HOME .. "_plus"
        if vim.fn.isdirectory(oneRunHomePlus) ~= 1 then
            code.mkdirs(oneRunHomePlus)
            vim.fn.writefile({ "" }, code.path_join(oneRunHomePlus, code.ONE_RUN_CMD_SETTINGS))
            vim.fn.writefile({ gradleGeneratedDir }, code.path_join(oneRunHomePlus, "arg01.txt"))

            local scriptId = "prepare_cache_of_gradle_generated_jar_task"
            code.writeScriptTemplate(scriptId, code.path_join(oneRunHomePlus, code.ONE_RUN_CMD_SCRIPT))

            local cmd = "cd " .. oneRunHomePlus .. " && " .. code.getOneRunCmd() .. " " .. scriptId .. " > " .. code.path_join(oneRunHomePlus, "out.txt")
            os.execute(cmd)
            code.log_info("   end command })> " .. cmd .. " <({")
        end
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

function code.lookupDevPlaceLite()
    code.PLACE_OF_SCRIPT_FULL_PATH = vim.fs.dirname(vim.fs.dirname(arg[0]))
end

function code.writeScriptTemplate(scriptTemplateName, toFile)
    code.lookupDevPlaceLite()
    local scriptTemplateFile = code.path_join(code.PLACE_OF_SCRIPT_FULL_PATH, "../plus/scriptTemplate/kt", scriptTemplateName .. ".kt")
    vim.fn.writefile(code.shift_(10, vim.fn.readfile(scriptTemplateFile)), toFile)
end

function code.lookupDevPlace()
    code.lookupDevPlaceLite()
    code.devPlaceRootFullPath = code.path_real(code.PLACE_OF_SCRIPT_FULL_PATH, "../../..")
    code.devPlaceRootName = vim.fs.basename(code.devPlaceRootFullPath)                                    -- https://neovim.io/doc/user/lua.html#_lua-module:-vim.fs
    code.devPlaceName = vim.fn.readfile(code.devPlaceRootFullPath .. "/.internal/place_config_desc")[1]
    code.devPlaceUid = "_u_" .. code.devPlaceRootName .. "__" .. code.devPlaceName
    code.devPlaceGenDir = code.path_join(code.devPlaceRootFullPath, "wwgen")
    code.devPlaceGenLnkDir = code.path_join(code.devPlaceGenDir, "lnk")
end

function code.ensure_dev_place_gen_lnk()
    code.log_debug("ensure_dev_place_gen_lnk 001:", code.devPlaceGenLnkDir)
    code.log_debug("ensure_dev_place_gen_lnk 001:", fileOfMarkerLinked)
    if vim.fn.isdirectory(code.devPlaceGenLnkDir) ~= 1 then
        code.log_info("mk ", code.devPlaceGenLnkDir)
        code.mkdirs(code.devPlaceGenLnkDir)
    else
        code.log_debug(code.devPlaceGenLnkDir, " exists")
    end
end

function code.ensure_app_by_script_place(appPseudoName)
    code.lookupDevPlace()
    code.ensureRunDir()
    code.RUN_PLACE_APP__TMP = code.path_join(code.runDir, code.devPlaceUid, "_orun_" .. appPseudoName .. "_")
    code.APP_BY_SCRIPT_NAME = vim.fs.basename(code.path_real(code.PLACE_OF_SCRIPT_FULL_PATH, ".."))
    code.APP_BY_SCRIPT__DIR = code.path_join(code.RUN_PLACE_APP__TMP, code.APP_BY_SCRIPT_NAME)
    code.APP_BY_SCRIPT__DIR_LINK = code.path_join(code.devPlaceGenLnkDir, "run_" .. appPseudoName .. "__" .. code.APP_BY_SCRIPT_NAME)
    code.APP_BY_SCRIPT_TOOL_FILE = code.path_join(code.APP_BY_SCRIPT__DIR, code.ONE_RUN_CMD_SCRIPT)

    code.ensure_dev_place_gen_lnk()

    code.mkdirs(code.APP_BY_SCRIPT__DIR)

    local fileMarker = "file_marker_from_app_by_script"
    local fileMarkerContent = code.devPlaceGenDir
    vim.fn.writefile({ fileMarkerContent }, code.path_join(code.RUN_PLACE_APP__TMP, "file_from_app_by_script"))
    vim.fn.writefile({ fileMarkerContent }, code.path_join(code.APP_BY_SCRIPT__DIR, fileMarker))
    vim.fn.writefile({ "" }, code.path_join(code.APP_BY_SCRIPT__DIR, code.ONE_RUN_CMD_SETTINGS))
    -- vim.fn.writefile({ "" }, code.APP_BY_SCRIPT_TOOL_FILE)
    local appDirLink = code.path_join(code.APP_BY_SCRIPT__DIR_LINK, fileMarker)
    code.log_debug("appDirLink:", appDirLink, "\n")
    -- code.log_info("appDirLink:", vim.fn.filereadable(appDirLink), "\n")                                   -- https://neovim.io/doc/user/builtin.html#filereadable()
    if vim.fn.filereadable(appDirLink) ~= 1 or fileMarkerContent ~= vim.fn.readfile(appDirLink)[1] then
        vim.uv.fs_unlink(code.APP_BY_SCRIPT__DIR_LINK)                                                       -- https://neovim.io/doc/user/luvref.html#uv.fs_unlink()
        vim.uv.fs_symlink(code.APP_BY_SCRIPT__DIR, code.APP_BY_SCRIPT__DIR_LINK)                             -- https://neovim.io/doc/user/luvref.html#uv.fs_symlink()
    end
    code.ensureOneRunCmd()
end

function code.echoAppsByScriptStartCmd(args)
    local appPseudoName = args[1]
    code.log_debug("name is:", appPseudoName)
    if nil == appPseudoName then
        code.exitWithErrorMsg(135, "name '" .. appPseudoName .. "' looks like undefined")
    end
    code.ensure_app_by_script_place(appPseudoName)
    local _args = code.shift_one(args)
    local apps_list = {}
    for _, v in ipairs(_args) do
        table.insert(apps_list, code.path_join(code.PLACE_OF_SCRIPT_FULL_PATH, v))
    end
    code.log_info("apps_list:", vim.inspect(apps_list), "\n")
    vim.fn.writefile(apps_list, code.path_join(code.APP_BY_SCRIPT__DIR, "apps_list.txt"))
    local scriptTemplateName = vim.fs.basename(code.PLACE_OF_SCRIPT_FULL_PATH)
    code.writeScriptTemplate(scriptTemplateName, code.APP_BY_SCRIPT_TOOL_FILE)
    io.output():write("cd " .. code.APP_BY_SCRIPT__DIR_LINK .. " && ")
    code.echoOneRunCmd(_args)
end

function code.ensureLinkToDirValid(pathOfLinkToDir, fileNameOfMarker, fileMarkerContent)
    if code.isTrue(code.fs.isLink(pathOfLinkToDir)) then
        local fileOfMarkerLinked = code.path_join(pathOfLinkToDir, fileNameOfMarker)
        code.log_debug("ensureLinkToDirValid 001:", fileOfMarkerLinked)
        --code.log_debug("ensureLinkToDirValid 001:", vim.fn.filereadable(fileOfMarkerLinked))
        --code.log_debug("ensureLinkToDirValid 001:", vim.uv.fs_realpath(fileOfMarkerLinked))
        if vim.fn.filereadable(fileOfMarkerLinked) == 1 and fileMarkerContent == vim.fn.readfile(fileOfMarkerLinked)[1] then
            code.log_debug("ensureLinkToDirValid 002")
            return 1
        else
            code.log_debug("ensureLinkToDirValid 003")
        end
    end
    code.log_debug("ensureLinkToDirValid 004")
    code.fs.rm_local_path(pathOfLinkToDir)
    return 0
end

function code.cleanupDirToBeLinked(dirPath, fileNameOfMarker, fileMarkerContent)
    code.fs.rm_local_path(dirPath)
    code.mkdirs(dirPath)
    vim.fn.writefile({ fileMarkerContent }, code.path_join(dirPath, fileNameOfMarker))
end

function code.link_out_place(args)
    code.lookupDevPlace()
    code.ensureRunDir()
    code.link_place_default("out")
end

function code.link_place_default(placeName)
    code.link_place(placeName, "file_from_app_by_script", code.devPlaceGenDir)
end

function code.link_place(placeName, fileNameOfMarker, fileMarkerContent)
    local dirInRun = code.path_join(code.runDir, code.devPlaceUid, "_" .. placeName .. "_")
    local dirInPrj = code.path_join(code.devPlaceGenLnkDir, placeName)

    code.log_debug("link_place dirInRun ", vim.fn.isdirectory(dirInRun), dirInRun)
    code.log_debug("link_place dirInPrj ", vim.fn.isdirectory(dirInPrj), dirInPrj)

    code.cleanupDirToBeLinked(dirInRun, fileNameOfMarker, fileMarkerContent)

    code.ensure_dev_place_gen_lnk()

    if code.isTrue(code.ensureLinkToDirValid(dirInPrj, fileNameOfMarker, fileMarkerContent)) then
        code.log_debug("looks like link :", dirInPrj, "to:", dirInRun, " is valid")
    else
        code.log_debug("link from:", dirInRun, "to:", dirInPrj)
        vim.uv.fs_symlink(dirInRun, dirInPrj)  -- https://neovim.io/doc/user/luvref.html#uv.fs_symlink()
    end
end

function code.open_in_ide(args, tool_ide)
    code.link_out_place(args)
    code.link_place("tool_info", "file_from_app_by_script_tool_ide", tool_ide .. ".sh")
    code.link_place_default("tool_run_store")
    io.output():write(tool_ide .. " -Dnosplash=true " .. code.path_join(code.devPlaceRootFullPath, code.devPlaceName))
end

function code.open_in_ide1(args)
    code.lookupOneRunCmd()
    code.open_in_ide(args, code.TOOL__IDE)
end

function code.open_in_ide2(args)
    code.lookupOneRunCmd()
    code.open_in_ide(args, code.TOOL_IDE2)
end

local debug = {}

function debug.oneLite(args)
    --print(vim.inspect(args), "\n")
    ----https://neovim.io/doc/user/luvref.html#uv.fs_symlink()
    --print(vim.inspect(vim.uv.fs_realpath), "\n")
    --print(vim.inspect(vim.uv.fs_symlink), "\n")
    --print(vim.inspect(vim.uv), "\n")
end

function debug.one(args)
    print(vim.inspect(args), "\n")
    print("SCRIPT                    ", arg[0])

    code.ensure_app_by_script_place("tmp_one")
    code.echoOneRunCmd(args)

    print("APP_BY_SCRIPT_NAME        ", code.APP_BY_SCRIPT_NAME)
    print("APP_BY_SCRIPT_TOOL_FILE   ", code.APP_BY_SCRIPT_TOOL_FILE)
    print("APP_BY_SCRIPT__DIR        ", code.APP_BY_SCRIPT__DIR)
    print("APP_BY_SCRIPT__DIR_LINK   ", code.APP_BY_SCRIPT__DIR_LINK)
    print("DEV_PLACE_GEN_DIR         ", code.devPlaceGenDir)
    print("DEV_PLACE_GEN_TMP         ", code.devPlaceGenLnkDir)
    print("devPlaceName              ", code.devPlaceName)
    print("devPlaceRootFullPath      ", code.devPlaceRootFullPath)
    print("devPlaceUid               ", code.devPlaceUid)
    print("JAVA_HOME                 ", code.JAVA_HOME)
    print("PLACE_OF_SCRIPT_FULL_PATH ", code.PLACE_OF_SCRIPT_FULL_PATH)
    print("RUN_DIR                   ", code.runDir)
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
        --["debug"]                      --[[]] = debug.oneLite,
        ["echoOneRunCmd"]              --[[]] = code.echoOneRunCmd,
        ["echoArgs"]                   --[[]] = code.echoArgs,
        ["echoAppsByScriptStartCmd"]   --[[]] = code.echoAppsByScriptStartCmd,
        ["link_out_place"]             --[[]] = code.link_out_place,
        ["open_in_ide"]                --[[]] = code.open_in_ide1,
        ["open_in_ide2"]               --[[]] = code.open_in_ide2,
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

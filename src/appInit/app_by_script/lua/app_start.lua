local code = {}

function code.str_trim_end(str)
    return string.gsub(str, "%s+$", "")
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

function code.path_real(...)
    local path = code.path_join(...)
    local process = assert(io.popen("test -e " .. path .. " && realpath " .. path .. " || echo -n " .. path, 'r'))
    local processOut = assert(process:read('*a'))
    -- local exitCode =
    process:close()
    local path_fixed = code.str_trim_end(processOut)
    return path_fixed
end

function code.main()
    print("start ", arg[0], "\n")
    local sh_file = code.path_real(vim.fs.dirname(arg[0]), "../app_lua_debug")
    print("start ", sh_file, "\n")
    os.execute("sh " .. sh_file)
end

code.main()

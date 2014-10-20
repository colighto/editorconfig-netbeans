[![](https://travis-ci.org/welovecoding/editorconfig-netbeans.svg?branch=master)](https://travis-ci.org/welovecoding/editorconfig-netbeans)

# EditorConfig NetBeans Plugin


A NetBeans IDE plugin supporting the [EditorConfig][] standard.


## EditorConfig Project

EditorConfig makes it easy to maintain the correct coding style when switching
between different text editors and between different projects.  The
EditorConfig project maintains a file format and plugins for various text
editors which allow this file format to be read and used by those editors.  For
information on the file format and supported text editors, see the
[EditorConfig website][EditorConfig].

## Example file

**.editorconfig**

```ini
# top-most EditorConfig file
root = true

# Unix-style newlines with a newline ending every file
[*]
end_of_line = lf
insert_final_newline = true

# 4 space indentation
[*.py]
indent_style = space
indent_size = 4

# Tab indentation (no size specified)
[*.js]
indent_style = tab

# Indentation override for all JS under lib directory
[lib/**.js]
indent_style = space
indent_size = 2

# Matches the exact files either package.json or .travis.yml
[{package.json,.travis.yml}]
indent_style = space
indent_size = 2
```

## Supported properties

- :construction_worker: charset
- :construction_worker: end_of_line
- :white_check_mark: indent_size
- :construction_worker: indent_style
- :white_check_mark: insert_final_newline
- :question: max_line_length
- :construction_worker: tab_width
- :construction_worker: trim_trailing_whitespace

[EditorConfig]: http://editorconfig.org

## Contributors

[![Benny Neugebauer on Stack Exchange][stack_exchange_flair_bennyn]][stack_exchange_link_bennyn]

[![Michael Koppen on Stack Exchange][stack_exchange_flair_yser]][stack_exchange_link_yser]


[stack_exchange_link_bennyn]: http://stackexchange.com/users/203782/benny-neugebauer?tab=accounts
[stack_exchange_link_yser]: http://stackexchange.com/users/3210455/yser?tab=accounts
[stack_exchange_flair_bennyn]: http://stackexchange.com/users/flair/203782.png?theme=default
[stack_exchange_flair_yser]: http://stackexchange.com/users/flair/3210455.png?theme=default

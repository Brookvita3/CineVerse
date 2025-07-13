package movies.rule

default allow = false

allow if {
  input.method == "POST"
  input.path == ["movies"]
  input.headers["X-Role"] == "admin"
}

allow if {
  input.method == "POST"
  count(input.path) == 3
  input.path[0] == "movies"
  input.path[2] == "upload-url"
  input.headers["X-Role"] == "admin"
}
package movies.rule

default allow = false

allow if {
  input.method == "POST"
  input.path == ["movies"]
  input.headers["X-Role"] == "admin"
}
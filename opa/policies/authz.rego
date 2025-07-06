package authz

default allow = false

allow if {
  input.method == "GET"
  input.path == ["movie"]
  input.user == "admin"
}
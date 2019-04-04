
def sp = '.+\\.(xlsx|xls)$'
def p = ~sp



test = 'abc.xlsx'
//assert ! (test =~ p)

assert p.matcher(test).matches()


test = 'abc.xls'
assert p.matcher(test).matches()
test = '.xlsx'
assert ! p.matcher(test).matches()
test = '.xls'
assert ! p.matcher(test).matches()
test = 'a.b.xls'
assert p.matcher(test).matches()
test = 'a.xls123'
assert ! p.matcher(test).matches()
test = 'a.xlsx123'
assert ! p.matcher(test).matches()


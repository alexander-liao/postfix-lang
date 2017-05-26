# postl

PostL is a postfix-notation stack-based golfing language inspired by many other golfing languages. There is a stack and another free-standing variable that can be used when it is inconvenient to put it onto the stack.

There are two types of literals: numbers and strings. A number is in the form of a Java float, formally `\d+(\.\d+)?` or \.\d+. A number cannot be written as a negative number directly. A string is in the form "..." or '...', and backslash-notation escapes characters in the exact same way as in Java. Writing a literal will push it onto the stack

All operators are in postfix notation. This is a complete list of all operators and what they do (an operator can be represented in multiple ways, and so the various names are space-separated)`:

`+ ADD SUM COMBINE`: Pops the top two items on the stack, adds them, and then pushes the sum onto the stack. Only works with numbers and strings (Note: for strings, this adds them in reverse order because of how stacks work)

`# ++ ADD_ALL`: Pops everything off the stack, adds them all, and then pushes the result onto the stack. If the stack only has numbers, the numbers are added; otherwise, the objects are turned to strings and added.

`- SUB SUBTRACT`: Pops the top two items on the stack, takes the difference, and then pushes the result onto the stack. Only works with numbers. Note that 2 3 - returns 1 because the objects are popped off in reverse order.

`N NEG NEGATE ADDINV ADDITIVE_INVERSE`: Pops the top of the stack off, negates it, and then pushes the result onto the stack. If the top is a number, it gets the negative (equivalent to N 0 -), otherwise it turns it to a string and reverses the string.

`* MULT PRODUCT MULTIPLY`: Pops the top two items on the stack, multiplies them, and then pushes the product onto the stack. Only works if the second item is a number (the first number as ordered in the program) is a number. If the first item is a number, it returns the product, otherwise it turns it to a string an repeats the string that many times (if the number is negative, it will have the strings reversed).

`M XX MULTIPLY_ALL`: Pops everthing off the stack, multiplies them all, and then pushes the result onto the stack. Only works if the stack only contains numbers.

`/ DIVIDE`: Pops the top two items on the stack, performs division, and then pushes the quotient onto the stack. Only works with numbers. Note that 1 2 / returns 2 because the objects are popped off in reverse order.

`I RECIP MULTINV RECIPROCAL MULTIPLICATIVE_INVERSE`: Pops the top of the stack off, takes the reciprocal of it, and then pushes the result onto the stack. Only works if the top is a number. This works with 0 as well, returning "Infinity"

`! FACT FACTORIAL`: Pops the top of the stack off, takes the factorial of the floor of that value, and then pushes the result onto the stack. Only works if the top is a number.

`P *\* EXPONENTIATE EXP POW POWER`: Pops the top two items on the stack, performs exponentiation, and then pushes the result onto the stack. Only works with numbers. Note that 3 4 ** returns 64 because the objects are popped off in reverse order.

`R <-> REVERSE`: Reverses the stack

`) RANGE`: Pops the top three items on the stack and pushes all numbers starting at the first number up to but not exceeding the second number, counting by the third number. Only works with numbers. If the third number is 0, the program will freeze.

`= SWITCH`: Pops the top three items on the stack and, depending on the first object, returns either the second or the third object. If the object is true, returns the second number, otherwise returns the third number. See "CONVERSION TO BOOLEAN" for how a value is determined to be true or false.

`D DEL`: Pops the top of the stack.

`. POP`: Pops the top of the stack and prints it.

`} PEEK`: Looks at the top of the stack and prints it without popping it.

`F BIT_FLIP`: Pops the top of the stack, flips all bits, and pushes the result onto the stack. Only works on numbers. For example, 7 F will return 0 because 7 = 111, 000 = 0. 10 F will return 5 because 10 = 1010, 0101 = 5.

`; JUMP GOTO`: Pops the top of the stack and returns a GOTO processor flag that will set the interpreter to that index. Only works for numbers.

`] ->| END TERMINATE`: Pushes an END processor flag that will terminate the program. Requires no input.

`_ FLR FLOOR`: Pops the top of the stack, takes the floor of it, and pushes the result back onto the stack. If the top is a number, it finds the largest integer smaller than it, otherwise it turns the value into a string and makes it all-lowercase.

`^ CEIL CEILING`: Pops the top of the stack, takes the ceiling of it, and pushes the result back onto the stack. If the top is a number, it finds the smallest integer larger than it, otherwise it turns the value into a string and makes it all-caps.

`~ ROUND`: Pops the top of the stack, rounds it, and pushes the result back onto the stack. If the top is a number, it finds the integer closest to it (formally, floor(num + 0.5)), otherwise it turns the value into a string and removes leading and trailing whitespace (trims it, in Python strips it)

`? RND RAND RANDOM`: Pushes a random number between 0 and 1 onto the stack using `java.lang.Math.random()`. Requires no input.

`, IN INPUT`: Reads from `System.in` and pushes the result onto the stack. Requires no input.

`$ NUM PARSE NUMERIFY`: Pops the top of the stack, turns it into a string, attempts to parse it to a float, and pushes that number onto the stack. Will throw a `NumberFormatException` if the top is not a valid float in base 10.

`R RADIX`: Pops the top two items on the stack, parses the first value as a string in base {second value}, and pushes that number onto the stack. The second value must be a positive integer.

`@ STR UNPARSE STRINGIFY`: Pops the top of the stack, turns it into a string, and pushes that value onto the stack. Essentially, `obj.toString()`.

`C COPY DUPLICATE`: Pops the top of the stack and pushes it back on twice. This is especially useful before operators that consume the value if you need to keep the value.

`% MOD MODULO`: Pops the top two items on the stack, takes the remainder when the first number is divided by the second, and pushes the result back onto the stack. Note that 2 3 % will return 1 because the objects are popped off in reverse order.

`: x10+ COMBINE COMBINE_DIGITS`: Pops the top two items on the stack, takes the sum of 10 times the first number and the second number, and pushes the result back onto the stack. Note that 2 3 `: will return 32 because the objects are popped off in reverse order.

`\ SWAP`: Pops the top two items on the stack and pushes them back on in reverse order. 2 3 \ gives 3 2.

`<backtick> SKIP`: Pushes a SKIP processor flag onto the stack, which, when processed, does nothing. Requires no input.

`E EMPTY EMPTY_STRING`: Pushes an empty string onto the stack. Requires no input.

`W PUSH WORKER`: Gets the value of the freestanding variable and pushes it onto the stack.

`< SET PULL`: Pops the top of the stack and assigns that value to the free variable.

`O CONTAINS`: Pops the top two items on the stack and pushes 1 onto the stack if the first string contains the second, otherwise pushes 0.

`B BLANK`: Pushes a blank-check flag onto the stack. When processed, it will push `1` onto the stack if the stack is empty; otherwise, it will push `0`.

`> FLAG START PROCESS`: Runs through the entire stack and executes all processor flags. If it reaches an END flag, it will terminate execution without executing the other flags.

`DEBUG`: Causes the intepreter to start print verbose processing data (current index and current stack on all operators). The significance of this operator is that it is the only operator without a single character representation.

## CONVERSION TO BOOLEAN:

    null -> false
    "" -> false
    0 -> false
    Any processor flag -> false

`Everything else -> true`

## CODING STYLE

Two operators can be written together like `RANDOMRANGE (RANDOM RANGE)`, as long as there "is no ambiguity"; that is, `RANDOM` is an operator but no operator exists that starts with `RANDOMR`. Operators are case-insensitive.

## EXAMPLE PROGRAM
    1 NEGATE 0 INPUT PARSE RANGE BLANK PROCESS SKIP SWAP TERMINATE SWAP SWITCH PROCESS COPY 2 SWAP EXPONENTIATE SWAP POP " ** 2 = " POP POP "\n" POP 29 GOTO PROCESS
    1N0,$)B>`\]\=>C2\P\." ** 2 = ".."\n".6;>

Reads number `n` from the input. Prints all squares up to `n ** 2`. How it works:

Pushes `1`, turns it to `-1`, pushes `0`, inputs, and parses. The stack is now `[-1 0 n]`.  
Generates a range from `n` to `0` counting by `-1`. The stack is now `[n n-1 n-2 ... 2 1]`.  
[Index 29]  
Checks if the stack is empty. Then, pushes `SKIP` and swaps it to second. Then, pushes `END` and swaps it to second. The stack is now `[n n-1 n-2 ... SKIP END <BLANK>]`.  
Switches on whether or not the stack is empty. If so, retrieve the `END` flag; else, retrieve the `SKIP` flag. The stack is now either `[n n-1 n-2 ... 2 1 SKIP]` or `[END]`.  
Process whatever flag is there. If the stack is empty, terminate the program, otherwise do nothing.  
Copy the top of the stack, push 2, swap, and exponentiate (in this case, square). The stack is now `[n n-1 n-2 ... k k**2]`, where `k` is the last element.  
Swap again. The stack is now `[n n-1 n-2 ... k**2 k]`.  
Pop `k`. Then, push and pop `" ** 2 = "`. Then, pop `k ** 2`. The stack is now `[n n-1 n-2 ... k+2 k+1]`.  
Go back to index 29, and keep repeating until the stack is emptied.  

# CHANGELOG

Version 2.0: More operators for more functionalty
- Removed "X" from the lookup-table of multiplication
+ New Operators: // TODO

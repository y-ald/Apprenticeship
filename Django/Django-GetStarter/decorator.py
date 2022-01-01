def outer_function(msg):
    message = msg
    def inner_function():
        print(message)
    return inner_function()

#outer_function() # return HI
# my_func = outer_function() return inner_function waiting to be execute
# my_func

#hi_funct = outer_function('Hi')
#bye_funct = outer_function('Bye')

#hi_funct
#bye_funct

# decorator is a function that take a function as an id and returning another function
def decorator_function(original_function):
    def wrapper_function(*args, **kwargs):
        print('wrapper executed this before {}'.format(original_function.__name__))
        return original_function(*args,**kwargs)
    return wrapper_function

@decorator_function()
def display():
    print('display function ran')

@decorator_function(args)
def display_info(name, age):
    print('display_info ran with arguments ({} {})'.format(name, age))
#decorator_display = decorator_function(display)
#decorator_display

display
display_info
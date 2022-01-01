from django.shortcuts import render
from django.utils.regex_helper import normalize
from .models import Book, Author, BookInstance, Genre

# Create your views here.
def index(request):
    """ views index"""
    num_books = Book.objects.all().counts
    num_instances = BookInstance.objects.all().counts()
    num_instances_available = BookInstance.objects.all().filter(status__exact='a')
    num_authors = Author.objects.all().counts

    context = {
        'num_books': num_books,
        'num_instances': num_instances,
        'num_instances_available': num_instances_available,
        'num_authors': num_authors,
    }

    return render(request, 'index.html', context=context)
    
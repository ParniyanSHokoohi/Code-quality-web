import requests

def count_pages(total, per_page):
    '''Return number of pages given total # of items and # of items per page.'''
    n_whole_pages, rest = divmod(total, per_page)
    # Add 1 page for rest items, if there are some left. 
    n_pages = n_whole_pages + int(bool(rest)) 
    return n_pages

def get_n_branches(repo):
    '''Return number of branches given repo dict.'''
    branches_url = repo['branches_url']
    # Remove placeholder for branch name from the URL.
    branches_list_url = branches_url.replace('{/branch}', '') 
    branches_list = requests.get(branches_list_url).json()
    n_branches = len(branches_list)
    return n_branches

def get_description_length(repo):
    '''Return number of characters in descriptio given repo dict.'''
    description = repo['description']
    description_length = len(description) if description else 0
    return description_length

def get_owner_name(repo):
    '''Return owner name stored in property given repo dict.'''
    owner = repo['owner']
    owner_name = owner['login']
    return owner_name
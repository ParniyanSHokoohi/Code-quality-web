'''
Collect data on n_repos repositories (per framework) that are found in GitHub 
by search_term, that use frameworks, thats main language is in allowed_languages.
'''

from ReposReader import ReposReader

search_term = 'online+shop'
frameworks = ['vue', 'react', 'angular']
allowed_languages = ['JavaScript', 'Vue', 'TypeScript']
n_repos = 330
ReposReader(search_term, frameworks, allowed_languages, n_repos).collect_data()
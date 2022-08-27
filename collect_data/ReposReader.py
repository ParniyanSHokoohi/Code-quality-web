import requests
import pandas as pd
import os
import helpers

class ReposReader:
    '''
    Search repositories on GitHub.
    Collect information about found repositories.
    Write collected data.
    Usage: run collect_data() method on an instance.
    '''
    def __init__(self, search_term, frameworks, allowed_languages, n_repos=100, write_dir='data'):
        '''_summary_

        Args:
            search_term (str): search term to search repos by
            frameworks (list(str)): data on repos that use this frameworks is collected
            allowed_languages (list(str)): only repos that have their main language in this list are collected
            n_repos (int, optional): desired minimum number of repos to collect per framework (actual number of collected repos may differ). Defaults to 100.
            write_dir (str, optional): path to directory to write data to. Defaults to 'data'.
        '''
        self.search_term = search_term
        self.frameworks = frameworks
        self.allowed_languages = set(allowed_languages)
        self.min_n_repos = n_repos
        self.write_dir = write_dir
        self.per_page = 100
        self.file_name_prefix = 'attributes_'
        self.api_url = 'https://api.github.com'
        self.route = 'search/repositories'
        self.all_data = pd.DataFrame()
        # Properties that can be directly read from repo object.
        self.read_props = ['name','full_name','clone_url','forks','watchers',
                                                        'open_issues','size']
        # Properties that need to be computed with functions.
        self.computed_props = {
            'n_branches': helpers.get_n_branches,
            'len_description': helpers.get_description_length,
            'owner': helpers.get_owner_name,
        }

    def __del__(self):
        '''Write collected data before deconstruction.'''
        file_name = os.path.join(self.write_dir, f'{self.file_name_prefix}all.csv')
        self.all_data.to_csv(file_name)
        print(f'Saved all data to {file_name}.')

    def collect_data(self):
        '''Collect and write data on repos for frameworks.'''
        for framework in self.frameworks:
            self.framework = framework
            self.count_pages_and_read_first_page()
            repos_data = []
            while(self.page_num <= self.n_pages and 
                    len(repos_data) <= self.min_n_repos):
                for repo in self.repos['items']:
                    if repo['language'] in self.allowed_languages:
                        repos_data.append(self.get_props(repo))
                self.read_page()
            self.write_data(repos_data)
    
    def set_current_url(self):
        '''
        Set url as GitHub API URL of the page_num page 
        of E-Commerce repos for framework.
        '''
        parameters = {
            'q': f'{self.search_term}+{self.framework}', # search_term
            'per_page': self.per_page,
            'page': self.page_num
        }
        query_parameters = '&'.join([f'{name}={value}' 
                                        for name,value in parameters.items()])
        self.url = f'{self.api_url}/{self.route}?{query_parameters}'
    
    def read_page(self):
        '''
        Get data from the next page returned by GitHub API 
        and increment page_num.
        '''
        self.set_current_url()
        self.repos = requests.get(self.url).json()
        self.page_num +=1
    
    def count_pages_and_read_first_page(self):
        self.page_num = 1
        self.read_page()
        n_repos = self.repos['total_count']
        self.n_pages = helpers.count_pages(n_repos, self.per_page)
    
    def get_props(self, repo):
        '''Return dictionary of read and computed props for repo.'''
        props = dict()
        for prop in self.read_props:
            props[prop] = repo[prop]
        for prop, compute_prop in self.computed_props.items():
            props[prop] = compute_prop(repo)
        return props
    
    def write_data(self, data):
        '''
        Write repos data for framework to CSV file 
        and add to data_frame that collects data for all frameworks.
        '''
        len_data = len(data)
        file_name = os.path.join(self.write_dir, f'{self.file_name_prefix}{self.framework}.csv')
        repos_data = pd.DataFrame.from_dict(data, orient='columns')
        repos_data['framework'] = [self.framework]*len_data
        print(f'Read data on {len_data} repos for {self.framework.capitalize()}.')
        repos_data.to_csv(file_name)
        print(f'Saved data to {file_name}.')
        self.all_data = pd.concat([self.all_data, repos_data], ignore_index=True)
 
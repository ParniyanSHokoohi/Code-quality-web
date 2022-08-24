import os
import pickle

for framework in ['vue', 'react', 'angular']:
    with open(f'repo_urls_{framework}.pkl', 'rb') as f:
        urls = pickle.load(f)
    for i, url in enumerate(urls):
        url_parts = url.split('/')
        repo_name = '_'.join(url_parts[-2:])
        repo_name = f'repos/{framework}/{repo_name}'
        os.system(f'git clone {url} {repo_name}')
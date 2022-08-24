import pickle
import requests
import pandas as pd

def crawl_urls(query='online+shop+vue', save_path='repo_urls_vue_api.pkl', N=330):
    allowed_languages = set(['JavaScript', 'Vue', 'TypeScript'])
    
    # Build URL.
    base_url = "https://api.github.com/search/repositories?q="
    parameters = "&per_page=100" 
    url = base_url + query + parameters
    
    # Call API.
    repos = requests.get(url).json() 
    
    # Count Pages.
    n_repos = repos['total_count']
    n_pages, rest = divmod(n_repos, 100)
    if rest:
        n_pages += 1

    # Get repos URLs and info.
    rows = []
    for page in range(2, n_pages + 1):
        if len(rows) > N:
            break
        for repo in repos['items']:
            language = repo['language']
            if  language in allowed_languages:
                attributes = dict()
                attributes["owner"] = repo['owner']['login']
                attributes["name"] = repo['name']
                attributes["full_name"] = repo['full_name']
                attributes["clone_url"] = repo['clone_url']
                attributes["forks"] = repo['forks']
                attributes["watchers"] = repo['watchers']
                attributes["open_issues"] = repo['open_issues']
                describtion = repo['description']
                # attributes["describtion"] = describtion
                attributes["describtion_length"] = 0
                if describtion:
                    attributes["describtion_length"] = len(describtion)
                attributes["size"] = repo['size']
                attributes["branches"] = len(requests.get(repo['branches_url'].
                                                replace('{/branch}', '')).json())
                if not attributes in rows:
                    rows.append(attributes)
        # Get next page
        url = f"{base_url}{query}{parameters}&page={page}"
        repos = requests.get(url).json()

    # Write data
    data = pd.DataFrame.from_dict(rows, orient='columns')       
    data.to_csv(f'attributes_{query}.csv')
    urls = data['clone_url'].to_list()
    with open(f'{save_path}','wb') as f:
        pickle.dump(urls, f)
    
    current_total = len(urls)
    print(f'{current_total} total links crawled.')


parameters = {
'vue': {
        'save_path': 'repo_urls_vue_js.pkl',
        },

'react': {
        'query': 'online+shop+react',
        'save_path': 'repo_urls_react.pkl',
        },
'angular': {
        'query': 'online+shop+angular',
        'save_path': 'repo_urls_angular.pkl',
        },
}

for framework, parameters in tqdm (parameters.items(), desc=f'Crawling URLs'):
    print(f'Current framework: {framework}')
    crawl_urls(**parameters)
            
import pandas as pd

dfs = []
for framework in ['vue', 'react', 'angular']:
    df = pd.read_csv(f'attributes_{framework}.csv', index_col=0)
    df['framework'] = [framework]*len(df)
    dfs.append(df)

attributes = pd.concat(dfs, ignore_index=True)
attributes.to_csv('attributes.csv')
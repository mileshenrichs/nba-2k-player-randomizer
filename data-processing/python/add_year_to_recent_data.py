# This script transforms data downloaded from sports-reference.com to be compatible 
# with the CSV column format used in the primary dataset (1950 - 2017).
# Example usage: python add_year_to_recent_data 2018 2018-season.csv

import sys
import csv

output_rows = []

with open(sys.argv[2]) as inputfile:
  readCSV = csv.reader(inputfile, delimiter=',')
  for row in readCSV:
    print(row)
    year = sys.argv[1]
    name = row[1][:row[1].index('\\')]
    position = row[2]
    team = row[4]
    games = row[5]
    points_scored = row[29]

    output_row = []
    for i in range(53):
      if i == 1:
        output_row.append(year)
      elif i == 2:
        output_row.append(name)
      elif i == 3:
        output_row.append(position)
      elif i == 5:
        output_row.append(team)
      elif i == 6:
        output_row.append(games)
      elif i == 52:
        output_row.append(points_scored)
      else:
        output_row.append('')

    output_rows.append(output_row)

out_filename = sys.argv[2][:len(sys.argv[2]) - 4] + '-out.csv'

with open(out_filename, 'w', newline='') as outputfile:
  writer = csv.writer(outputfile)
  writer.writerows(output_rows)
# cv19api
### De-biasing NHS England death reports

A problem with death reporting right now is that the daily figures that get publicised in the media *do not* represent
the number of deaths that occurred that day. In fact, many deaths can take as much as 10 days to filter through into
these reports. Interpreting the reported death figures can give a false impression about the crisis we face.

The point is illustrated below. Using the 11th April data from NHS England, this is a graph of the number of days since the
reported death actually occurred.

![lag_distribution](api/img/lag_distribution.png)

You can see that about 40% of cases take 2 days to report, but there is a long tail on the distribution. Assuming that 
this is the distribution, in order to get a ~95% accurate picture of daily deaths, we have to take at least the 7 days 
of data that come after this date.


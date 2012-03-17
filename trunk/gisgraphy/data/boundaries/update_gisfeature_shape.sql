update gisfeature g set shape=(select shape from openstreetmap o where g.name=o.name and o.shape && g.location);

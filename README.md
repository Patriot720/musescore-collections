# collections-musescore

[![Netlify Status](https://api.netlify.com/api/v1/badges/8eb788bf-39ef-459a-b5fb-7e5307b0a64c/deploy-status)](https://app.netlify.com/sites/musescore-collections/deploys) [![demo website](https://img.shields.io/badge/website-up-green)](https://musescore-collections.netlify.com) 

## Overview

App for sorting musical scores from musescore.com in collections 

![Collections](https://i.imgur.com/hEI3z0o.png)

## Building locally

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL. 

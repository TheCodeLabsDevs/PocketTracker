<!doctype html>
<html>
    <head>
        <#import "spring.ftl" as s/>

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="<@s.url "/webjars/bootstrap/5.0.0-beta1/css/bootstrap.min.css"/>" rel="stylesheet"/>
        <script src="<@s.url "/webjars/bootstrap/5.0.0-beta1/js/bootstrap.bundle.min.js"/>"></script>

        <title>PocketTracker - Home</title>

        <link href="<@s.url "/css/main.css"/>" rel="stylesheet">
    </head>

    <body>
        <#import "navbar.ftl" as navbar>
        <@navbar.navbar/>

        <main>
            <div class="py-5 bg-light">
                <div class="container">
                    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
                        <#list shows as show>
                            <div class="col">
                                <div class="card shadow-sm">
                                    <svg class="bd-placeholder-img card-img-top" width="100%"
                                         xmlns="http://www.w3.org/2000/svg" role="img" aria-label="Placeholder: Thumbnail"
                                         preserveAspectRatio="xMidYMid slice" focusable="false">
                                        <rect width="100%" height="100%" fill="#55595c"/>
                                        <text x="50%" y="50%" fill="#eceeef" dy=".3em">Thumbnail</text>
                                    </svg>

                                    <div class="card-body">
                                        <p class="card-text fw-bold">${show.getName()}</p>
                                        <p class="card-text">
                                            ${show.getSeasons()?size} Staffeln - ${showService.getTotalNumberOfEpisodes(show)} Episoden
                                        </p>
                                        <p class="card-text">
                                            <div class="progress">
                                                <div class="progress-bar" role="progressbar" style="width: 75%;" aria-valuenow="75" aria-valuemin="0" aria-valuemax="100">65/87</div>
                                            </div>
                                        </p>
                                        <div class="d-flex justify-content-between align-items-center">
                                            <button type="button" class="btn btn-sm btn-outline-secondary">Details</button>
                                            <button type="button" class="btn btn-sm btn-outline-secondary">Add</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </#list>
                    </div>
                </div>
            </div>
        </main>
    </body>
</html>

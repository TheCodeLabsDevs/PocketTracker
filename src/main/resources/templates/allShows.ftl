<!doctype html>
<html>
    <head>
        <#import "header.ftl" as headerMacros>
        <@headerMacros.header 'Home'/>
        <#import "helpers.ftl" as helperMacros>
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
                                    <@helperMacros.imagePlaceholder />

                                    <div class="card-body">
                                        <p class="card-text fw-bold">${show.getName()}</p>
                                        <p class="card-text">
                                            ${show.getSeasons()?size} Staffeln
                                            - ${showService.getTotalNumberOfEpisodes(show)} Episoden
                                        </p>
                                        <div class="d-flex justify-content-between align-items-center">
                                            <button type="button" class="btn btn-sm btn-outline-secondary">Details
                                            </button>
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

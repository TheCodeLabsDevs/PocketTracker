<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "helpers.ftl" as helperMacros>

    <#import "/common/head.ftl" as headMacros>
    <@headMacros.head 'Episodes'/>

    <body class="bg-light">
        <#import "/common/navbar.ftl" as navbar>
        <@navbar.navbar/>

        <main>
            <div class="py-5">
                <div class="container">
                    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">

                        <#list shows as show>
                            <div class="col">
                                <div class="card shadow-sm">
                                    <#if show.getBannerPath()??>
                                        <img src="<@s.url "/resources/" + show.getBannerPath()/>" class="card-img-top"/>
                                    <#else>
                                        <@helperMacros.imagePlaceholder />
                                    </#if>

                                    <div class="card-body">
                                        <p class="card-text fw-bold">${show.getName()}</p>
                                        <p class="card-text">
                                            ${show.getSeasons()?size} Staffeln
                                            - ${showService.getTotalNumberOfEpisodes(show)} Episoden
                                        </p>
                                        <div class="d-flex justify-content-between align-items-center">
                                            <a href="<@s.url "/show/" + show.getId()/>" type="button"
                                               class="btn btn-sm btn-outline-secondary stretched-link">Details</a>
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

<!doctype html>
<html>
    <head>
        <#import "spring.ftl" as s/>
        <#import "helpers.ftl" as helperMacros>
        <#import "header.ftl" as headerMacros>
        <@headerMacros.header 'Show'/>
    </head>

    <body class="bg-light">
        <#import "navbar.ftl" as navbar>
        <@navbar.navbar/>

        <main>
            <div class="py-5">
                <div class="container">
                    <div class="card shadow-sm">
                        <div class="card-body">
                            <h3 class="card-title text-center">${show.getName()} (${show.getFirstAired()?date('yyy-MM-dd')?string.yyyy})</h3>
                            <div class="row mt-3 mt-md-5">
                                <div class="col-12 col-md-4">
                                    <#if show.getPosterPath()??>
                                        <img src="<@s.url "/resources/" + show.getPosterPath()/>" class="img-fluid"/>
                                    <#else>
                                        <@helperMacros.imagePlaceholder />
                                    </#if>
                                </div>

                                <div class="col-12 col-md-8 mt-3 mt-md-0">
                                    <div class="row">
                                        <@factItem "fas fa-folder" show.getSeasons()?size "Staffeln"/>
                                        <@factItem "fas fa-film" showService.getTotalNumberOfEpisodes(show) "Episoden"/>
                                        <@factItem "fas fa-hourglass" showService.getTotalPlayTime(show) "Minuten"/>
                                    </div>

                                    <div class="mt-3 mt-md-5">
                                        <h5>Beschreibung</h5>
                                        <#if show.getDescription()??>
                                            ${show.getDescription()}
                                        </#if>
                                    </div>

                                    <!-- TODO: list seasons as links -->
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </body>
</html>


<#macro factItem icon value description>
    <div class="col-4 text-center">
        <i class="${icon} fs-3"></i>
        <div class="fs-4">
            ${value}&nbsp;
            <span class="d-md-none"><br></span>
            ${description}
        </div>
    </div>
</#macro>

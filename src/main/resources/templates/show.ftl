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
                    <div class="mx-auto text-center mb-4">
                        <a href="<@s.url "/"/>" class="btn btn-primary" type="button"><i class="fas fa-arrow-left"></i>
                            Back</a>
                    </div>

                    <div class="card shadow-sm">
                        <div class="card-body">
                            <h3 class="card-title text-center">${show.getName()}
                                (${show.getFirstAired()?date('yyy-MM-dd')?string.yyyy})</h3>
                            <div class="row mt-3 mt-md-5">
                                <div class="col-12 col-md-4 text-center">
                                    <#if show.getPosterPath()??>
                                        <img src="<@s.url "/resources/" + show.getPosterPath()/>"
                                             class="img-fluid w-50"/>
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

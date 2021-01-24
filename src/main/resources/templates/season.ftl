<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>
    <#import "/common/template.ftl" as template>
    <#import "/common/macros/show.ftl" as showMacros/>

    <@template.head season.getName() + " - " + season.getShow().getName()/>

    <body class="bg-light">
        <#import "/common/navbar.ftl" as navbar>
        <@navbar.navbar/>

        <main>
            <div class="container mt-5">
                <div class="row mb-4">
                    <div class="col-sm-12 col-md-8 col-lg-6 mx-auto text-center">
                        <a href="<@s.url "/show/" + season.getShow().getId()/>" class="btn btn-primary mb-4" role="button"><i class="fas fa-arrow-left"></i> Back</a>
                        <h2 class="mb-2 text-truncate">${season.getShow().getName()}</h2>
                        <h4 class="mb-4">-${season.getName()}-</h4>

                        <#assign numberOfWatchedEpisodes=userService.getWatchedEpisodesBySeason(currentUser, season)?size/>
                        <#assign totalNumberOfEpisodes=season.getEpisodes()?size/>
                        <@showMacros.progessBar numberOfWatchedEpisodes totalNumberOfEpisodes/>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-12 col-md-8 col-lg-6 mx-auto">
                        <div class="list-group list-episodes">
                            <#list season.getEpisodes() as episode>
                                <a href="<@s.url "/episode/" + episode.getId()/>" class="list-group-item list-group-item-action w-100 p-3">
                                    <div class="row">
                                        <div class="col-10 fw-bold text-truncate">
                                            ${episode.getNumber()} - ${episode.getName()}
                                        </div>
                                        <div class="col-2 d-flex justify-content-end">
                                            <input class="form-check-input episodeLink" data-url="<@s.url "/user/episode/" + episode.getId() + "/toggle/season"/>" type="checkbox" value="" <#if userService.isWatchedEpisode(currentUser, episode)>checked</#if>>
                                        </div>
                                    </div>
                                </a>
                            </#list>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </body>
</html>

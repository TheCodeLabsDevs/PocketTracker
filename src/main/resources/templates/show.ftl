<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/macros/show.ftl" as showMacros/>
    <#import "/users/form.ftl" as form>

    <@template.head show.getName()/>
    <@template.body>
        <div class="mx-auto text-center mb-4">
            <a href="<@s.url "/"/>" class="btn btn-primary" role="button"><i class="fas fa-arrow-left"></i>
                Back</a>
        </div>

        <div class="card shadow-sm">
            <div class="card-body">
                <h3 class="card-title text-center">${show.getName()}
                    (${show.getFirstAired()?date('yyy-MM-dd')?string.yyyy})</h3>
                <div class="row mt-3 mt-md-5">
                    <div class="col-12 col-md-4 text-center">
                        <#if show.getPosterPath()??>
                            <img src="<@s.url "/resources/" + show.getPosterPath()/>" class="img-fluid w-50"/>
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

                        <div class="col-12 col-md-11 my-3 my-md-5">
                            <div class="list-group list-episodes">
                                <#list show.getSeasons() as season>
                                    <a href="<@s.url "/season/" + season.getId() />" class="list-group-item list-group-item-action w-100 p-3">
                                        <div class="row">
                                            <div class="col-10 fw-bold text-truncate">
                                                ${season.getNumber()} - ${season.getName()}
                                            </div>
                                            <div class="col-2 d-flex justify-content-end">
                                                ${userService.getWatchedEpisodesBySeason(currentUser, season)?size}/${season.getEpisodes()?size}
                                            </div>
                                        </div>
                                        <div class="row mt-2">
                                            <div class="col">
                                                <#assign numberOfWatchedEpisodes=userService.getWatchedEpisodesBySeason(currentUser, season)?size/>
                                                <#assign totalNumberOfEpisodes=season.getEpisodes()?size/>
                                                <@showMacros.progessBar numberOfWatchedEpisodes totalNumberOfEpisodes/>
                                            </div>
                                        </div>
                                    </a>
                                </#list>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </@template.body>
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

<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/macros/movie.ftl" as movieMacros/>

    <@template.head movie.getName()/>
    <@template.body>
        <@b.back_button url="/user/movies" center=true/>

        <div class="card shadow-sm">
            <div class="card-body">
                <h3 class="card-title text-center">${movie.getName()} (${movie.getReleaseDate()?date('yyy-MM-dd')?string["dd.MM.yyyy"]})</h3>

                <#if userService.getWatchDateForMovie(currentUser, movie)??>
                    <div class="text-center"><@b.localize "movie.lastWatched"/> ${userService.getWatchDateForMovie(currentUser, movie)}</div>
                </#if>

                <@b.hasPermission "ADMIN">
                    <div class="text-center mt-2">
                        <@b.button label="button.edit" url="/movie/" + movie.getId() + "/edit" style="btn-sm btn-outline-primary" margin="" icon="fas fa-pencil"/>
                    </div>
                </@b.hasPermission>

                <div class="row mt-3 mt-md-5">
                    <div class="col-12 col-md-4 text-center">
                        <@b.row>
                            <@b.col>
                                <#if movie.getPosterPath()??>
                                    <img src="<@s.url "/resources/" + movie.getPosterPath()/>" class="img-fluid w-50"/>
                                <#else>
                                    <img src="<@s.url "/image/placeholder_poster.jpg"/>" class="img-fluid w-50"/>
                                </#if>
                            </@b.col>
                            <@b.col>
                                <a href="<@s.url "/user/movies/add/" + movie.getId()/>" role="button" class="btn btn-sm btn-outline-success mt-4 w-50 <#if isAdded>d-none</#if>"><@s.message "button.add"/></a>
                            </@b.col>
                        </@b.row>
                    </div>

                    <div class="col-12 col-md-8 mt-3 mt-md-0">
                        <div class="row">
                            <@movieMacros.factItem "fas fa-hourglass" movie.getLengthInMinutes() "factItem.duration.minutes"/>
                        </div>

                        <div class="row mt-3 mt-md-5 mb-4 mb-md-0">
                            <div class="col-12 col-md-11">

                                <h4><@b.localize "movie.description"/></h4>
                                <#if movie.getDescription()??>
                                    ${movie.getDescription()}
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </@template.body>
</html>

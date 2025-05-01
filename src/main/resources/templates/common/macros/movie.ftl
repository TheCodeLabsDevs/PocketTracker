<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>

<#macro movieCard movie userMovies isUserSpecific>
    <div class="col">
        <div class="card shadow-sm">
            <#if movie.getPosterPath()??>
                <img src="<@s.url "/resources/" + movie.getPosterPath()/>" class="card-img-top"/>
            <#else>
                <img src="<@s.url "/image/placeholder_poster.jpg"/>" class="card-img-top"/>
            </#if>

            <div class="card-body">
                <p class="card-text fw-bold">
                    ${movie.getName()}
                </p>

                <#if isUserSpecific && userService.getWatchDateForMovie(currentUser, movie)??>
                    <div class="mb-4">
                        <div class="text-center"><@b.localize "movie.lastWatched"/> ${userService.getWatchDateForMovie(currentUser, movie)}</div>
                    </div>
                </#if>

                <div class="d-flex justify-content-between align-items-center">
                    <a href="<@s.url "/movie/" + movie.getId()/>" role="button" class="btn btn-sm btn-outline-primary stretched-link"><@b.localize "button.details"/></a>

                    <div class="d-flex justify-content-end">
                        <#if isUserSpecific>
                            <a href="<@s.url "/user/movies/remove/" + movie.getId()/>" role="button" class="btn btn-sm btn-outline-danger z-index-above-stretched-link"><@s.message "button.remove"/></a>
                        <#else>
                            <@b.hasPermission "ADMIN">
                                <a href="<@s.url "/movie/" + movie.getId() + "/edit"/>" role="button" class="btn btn-sm btn-outline-danger z-index-above-stretched-link"><@b.localize "button.edit"/></a>
                            </@b.hasPermission>
                            <a href="<@s.url "/user/movies/add/" + movie.getId()/>" role="button" class="btn btn-sm btn-outline-success z-index-above-stretched-link <#if userMovies?seq_contains(movie)>d-none</#if> ms-2"><@s.message "button.add"/></a>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro factItem icon value description classes="" localizedDescription=true>
    <div class="col text-center ${classes}">
        <i class="${icon} fs-4"></i>
        <div class="fs-5">
            ${value}&nbsp;
            <span class="d-md-none"><br></span>
            <#if localizedDescription>
                <@b.localize description/>
            <#else>
                ${description}
            </#if>
        </div>
    </div>
</#macro>
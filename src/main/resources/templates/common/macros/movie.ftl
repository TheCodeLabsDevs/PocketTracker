<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>

<#macro movieCard movie>
    <div class="col">
        <div class="card shadow-sm">
            <#if movie.getPosterPath()??>
                <img src="<@s.url "/resources/" + movie.getPosterPath()/>" class="card-img-top"/>
            <#else>
                <img src="<@s.url "/image/placeholder_poster.jpg"/>" class="card-img-top"/>
            </#if>

            <div class="card-body">
                <p class="card-text fw-bold movie-name">
                    ${movie.getName()}
                </p>

                <div class="d-flex justify-content-between align-items-center">
                    <a href="<@s.url "/movie/" + movie.getId()/>" role="button" class="button-square btn btn-sm btn-outline-primary stretched-link"><i class="fas fa-info"></i></a>

                    <#if userService.getWatchDateForMovie(currentUser, movie)??>
                        <div class="text-center text-body-secondary last-watched">
                            <@b.localize "movie.lastWatched"/> ${userService.getWatchDateForMovie(currentUser, movie)}
                        </div>
                    </#if>

                    <div class="d-flex justify-content-end">
                        <@b.hasPermission "ADMIN">
                            <a href="<@s.url "/movie/" + movie.getId() + "/edit"/>" role="button" class="button-square btn btn-sm btn-outline-secondary z-index-above-stretched-link"><i class="fas fa-edit"></i></a>
                        </@b.hasPermission>

                        <#if userService.isMovieAdded(currentUser, movie.id)>
                            <a href="<@s.url "/user/movies/remove/" + movie.getId()/>" role="button" class="button-square btn btn-sm btn-outline-success z-index-above-stretched-link ms-2"><i class="fas fa-heart"></i></a>
                        <#else>
                            <a href="<@s.url "/user/movies/add/" + movie.getId()/>" role="button" class="button-square btn btn-sm btn-outline-success z-index-above-stretched-link ms-2"><i class="far fa-heart"></i></a>
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
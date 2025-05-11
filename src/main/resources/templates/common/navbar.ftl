<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>

<#macro navbar>
    <@f.form name="logout-form" url="/logout" classes="hide"></@f.form>

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top">
        <div class="container-fluid">
            <a class="navbar-brand" href="<@s.url "/"/>">
                <img src="<@s.url "/image/PocketTracker.png"/>" alt="" width="35" height="35"
                     class="d-inline-block align-top">
                PocketTracker - v${version}
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse text-center" id="navbarSupportedContent">
                <#if currentUser??>
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <@item name="menu.allShows" url="/shows"/>
                        <@item name="menu.allMovies" url="/movies"/>
                        <@item name="menu.myShows" url="/user/shows" additionalClasses="ms-3"/>
                        <@item name="menu.myMovies" url="/user/movies"/>
                        <@item name="menu.statistics" url="/user/statistics" additionalClasses="ms-3"/>

                        <@b.hasPermission "ADMIN">
                            <@dropdown name="menu.administration">
                                <@item name="menu.administration.users" url="/users/administration" subItem=true/>
                                <@item name="menu.administration.backup" url="/administration/backup" subItem=true/>
                                <@item name="menu.administration.batch.edit" url="/administration/batchEdit" subItem=true/>
                                <@item name="menu.administration.apis" url="/administration/apiConfiguration" subItem=true/>
                            </@dropdown>
                        </@b.hasPermission>
                    </ul>

                    <div class="me-auto"></div>

                    <@f.form name="logout-form" url="/search" classes="d-flex mt-3 mb-2 my-md-0 me-md-3">
                        <#if isUserSpecificView?? && isUserSpecificView>
                            <input type="hidden" name="isUserSpecificView" value="1"/>
                        </#if>
                        <#if isShowPage?? && isShowPage>
                            <input type="hidden" name="isShowPage" value="1"/>
                        </#if>
                        <input class="form-control" type="search" placeholder="<@b.localize "search"/>" aria-label="Suche" name="searchText">
                    </@f.form>
                    <div class="text-white mx-md-3 my-2 my-md-0 dropdown">
                        <a href="<@s.url "/user/settings"/>" class="link-light text-decoration-none"><i class="fas fa-user pe-3"></i>${currentUser.name}
                        </a>
                    </div>

                    <a class="btn btn-primary" onclick="document.getElementById('logout-form').submit();">Logout</a>
                </#if>
            </div>
        </div>
    </nav>
</#macro>

<#macro dropdown name>
    <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            <@b.localize name/>
        </a>
        <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
            <#nested>
        </ul>
    </li>
</#macro>

<#macro item name url icon="" markAsActiveByName=false subItem=false additionalClasses=''>
    <#assign isActive=false/>
    <#if markAsActiveByName>
        <#if currentPage?? && currentPage == name>
            <#assign isActive=true/>
        </#if>
    <#elseif springMacroRequestContext.getRequestUri()?starts_with(springMacroRequestContext.contextPath + url)>
        <#assign isActive=true/>
    </#if>

    <li class="<#if !subItem>nav-item</#if> ${additionalClasses}">
        <a class="<#if subItem>dropdown-item<#else>nav-link</#if> <#if isActive>active</#if>" href="<@s.url url/>">
            <#if icon?has_content><i class="${icon}"></i></#if>
            <@b.localize name/>
        </a>
    </li>
</#macro>

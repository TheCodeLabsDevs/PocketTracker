<#import "/spring.ftl" as s/>

<#macro navbar>
    <form class="hide" id="logout-form" action="<@s.url '/logout'/>" method="post">
        <#if _csrf??>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </#if>
    </form>

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top">
        <div class="container-fluid">
            <a class="navbar-brand" href="<@s.url "/"/>">
                <img src="<@s.url "/image/PocketTracker.png"/>" alt="" width="35" height="35"
                     class="d-inline-block align-top">
                PocketTracker - v1.0.0
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse text-center" id="navbarSupportedContent">
                <#if currentUser??>
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <@item "Alle Serien" "/shows"/>
                        <@item "Meine Serien" "/user/shows"/>
                        <@item "Statistiken" "/statistics"/>

                        <#if currentUser?? && currentUser.userRole.name() == "ADMIN">
                            <@item "Benutzerverwaltung" "/users/administration"/>
                        </#if>
                    </ul>

                    <div class="me-auto"></div>

                    <form class="d-flex mt-3 mb-2 my-md-0 me-md-3">
                        <input class="form-control" type="search" placeholder="Search" aria-label="Search">
                    </form>
                    <div class="text-white mx-md-3 my-2 my-md-0 dropdown">
                        <a href="<@s.url "/user/settings"/>" class="link-light text-decoration-none"><i class="fas fa-user pe-3"></i>${currentUser.name}</a>
                    </div>

                    <a class="btn btn-primary" onclick="document.getElementById('logout-form').submit();">Logout</a>
                </#if>
            </div>
        </div>
    </nav>
</#macro>

<#macro item name url icon="">
    <li class="nav-item">
        <a class="nav-link <#if springMacroRequestContext.getRequestUri()?contains(url)>active</#if>" href="<@s.url url/>">
            <#if icon?has_content><i class="${icon}"></i></#if>
            ${name}
        </a>
    </li>
</#macro>

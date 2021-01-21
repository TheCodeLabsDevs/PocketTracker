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
                <div class="me-auto"></div>
                <form class="d-flex mt-3 mb-2 my-md-0 me-md-3">
                    <input class="form-control " type="search" placeholder="Search" aria-label="Search">
                </form>
                <#if currentUser??>
                    <div class="text-white mx-md-3 my-2 my-md-0">
                        <i class="fas fa-user pe-3"></i>${currentUser.name}
                    </div>
                    <a class="btn btn-primary" onclick="document.getElementById('logout-form').submit();">Logout</a>
                </#if>
            </div>
        </div>
    </nav>
</#macro>

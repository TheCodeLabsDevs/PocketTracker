<#import "spring.ftl" as s/>

<#macro navbar>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <img src="<@s.url "/image/PocketTracker.png"/>" alt="" width="35" height="35"
                     class="d-inline-block align-top">
                PocketTracker - v1.0.0
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                </ul>
                <form class="d-flex">
                    <input class="form-control me-3" type="search" placeholder="Search" aria-label="Search">
                </form>
                <div class="text-white mx-3">
                    <i class="fas fa-user pe-3"></i>John Doe
                </div>
                <a class="btn btn-primary" href="<@s.url "logout"/>">Logout</a>
            </div>
        </div>
    </nav>
</#macro>

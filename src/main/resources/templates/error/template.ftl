<#import "/common/components/base.ftl" as b/>

<#macro errorView header message statusCode>
  <div class="align-middle">
    <div class="text-center">
      <h1 class="display-1 fw-bold">${statusCode}</h1>
      <p class="fs-3"><span class="text-danger"><@b.localize "error.oops"/></span> <@b.localize header/>
      </p>
      <p class="lead">
          <@b.localize message/>
      </p>
        <@b.button label="menu.myShows" url="/"/>
    </div>
  </div>
</#macro>
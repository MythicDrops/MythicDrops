<template>
  <div class="content">
    <div class="content-container">
      <main class="main">
        <div class="vp-doc" flex flex-col items-center mt-10>
          <h2 id="sponsored-by" op50 font-normal pt-5 pb-2>Sponsored by</h2>
          <div grid="~ cols-1 lg:cols-1" w-full text-center mt-10>
            <div class="flex flex-col">
              <a
                text-lg
                h="32px"
                href="https://github.com/sponsors/ToppleTheNun"
                rel="noopener noreferrer"
                >Richard's Sponsors</a
              >
              <a
                href="https://cdn.jsdelivr.net/gh/ToppleTheNun/static/sponsors.svg"
                target="_blank"
                rel="noopener noreferrer"
              >
                <img
                  crossorigin="anonymous"
                  inline-block
                  width="768"
                  height="1464"
                  class="resizable-img"
                  loading="lazy"
                  src="/sponsors/ToppleTheNun.svg"
                  alt="Richard Harrah's sponsors"
                />
              </a>
            </div>
          </div>
          <h2 id="sponsored-by" op50 font-normal pt-5 pb-2>Hosted by</h2>
          <p class="text-center opacity-75">
            <a href="https://vercel.com" rel="noopener noreferrer">
              <img
                src="/vercel-logotype-dark.svg"
                alt="Hosted by Vercel"
                width="114"
                height="151"
              />
            </a>
          </p>
        </div>
      </main>
    </div>
  </div>
</template>

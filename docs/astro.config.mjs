import starlight from "@astrojs/starlight";
import vercel from "@astrojs/vercel/serverless";

import { defineConfig } from "astro/config";
import starlightLinksValidator from "starlight-links-validator";

const VERCEL_PREVIEW_SITE =
  process.env.VERCEL_ENV !== "production" &&
  process.env.VERCEL_URL ?
  `https://${process.env.VERCEL_URL}` : undefined;
const site = VERCEL_PREVIEW_SITE ?? "https://mythicdrops.dev";

// https://astro.build/config
export default defineConfig({
  site,
  integrations: [
    starlight({
      title: "MythicDrops",
      editLink: {
        baseUrl: "https://github.com/MythicDrops/MythicDrops/edit/main/docs/",
      },
      social: {
        github: "https://github.com/MythicDrops/MythicDrops",
        discord: "https://discord.gg/ANu94t2B9C",
      },
      sidebar: [
        {
          label: "Start Here",
          items: [
            {
              label: "Installation",
              link: "/installation/",
            },
            {
              label: "Commands",
              link: "/commands/",
            },
            {
              label: "Permissions",
              link: "/permissions/",
            },
          ],
        },
        {
          label: "Guides",
          autogenerate: {
            directory: "guides",
          },
        },
        {
          label: "Features",
          autogenerate: {
            directory: "features",
          },
        },
        {
          label: "Resources",
          autogenerate: {
            directory: "resources",
          },
        },
        {
          label: "Config Reference",
          autogenerate: {
            directory: "config",
          },
        },
        {
          label: "Recipes",
          autogenerate: {
            directory: "recipes",
          },
        },
        {
          label: "Developer Reference",
          autogenerate: {
            directory: "developers",
          },
        },
      ],
    }),
  ],
  plugins: [starlightLinksValidator()],
  output: "server",
  adapter: vercel({
    webAnalytics: {
      enabled: true,
    },
  }),
});

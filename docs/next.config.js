const withNextra = require("nextra")({
  theme: "nextra-theme-docs",
  themeConfig: "./theme.config.tsx",
  staticImage: true,
  defaultShowCopyCode: true,
});

module.exports = withNextra({
  i18n: {
    locales: ["en-US"],
    defaultLocale: "en-US",
  },
  reactStrictMode: true,
  redirects: () => [
    {
      source: "/guide",
      destination: "/guide/installation",
      statusCode: 301,
    },
    {
      source: "/docs",
      destination: "/guide/installation",
      statusCode: 301,
    },
    {
      source: "/config",
      destination: "/config/config.yml",
      statusCode: 301,
    },
    {
      source: "/docs/commands",
      destination: "/guide/commands",
      statusCode: 301,
    },
    {
      source: "/docs/permissions",
      destination: "/guide/permissions",
      statusCode: 301,
    },
    {
      source: "/docs/weight",
      destination: "/guide/concepts/weight",
      statusCode: 301,
    },
    {
      source: "/docs/socket-extenders",
      destination: "/guide/features/sockets/extenders",
      statusCode: 301,
    },
    {
      source: "/docs/socket-gem-combiners",
      destination: "/guide/features/sockets/combiners",
      statusCode: 301,
    },
    {
      source: "/docs/worldguard-support",
      destination: "/guide/integrations/worldguard",
      statusCode: 301,
    },
    {
      source: "/docs/aura_gems",
      destination: "/guide/recipes/aura-gems",
      statusCode: 301,
    },
    {
      source: "/docs/config_yml",
      destination: "/config/config.yml",
      statusCode: 301,
    },
    {
      source: "/docs/creatureSpawning_yml",
      destination: "/config/creatureSpawning.yml",
      statusCode: 301,
    },
    {
      source: "/docs/customItems_yml",
      destination: "/config/customItems.yml",
      statusCode: 301,
    },
    {
      source: "/docs/identifying_yml",
      destination: "/config/identifying.yml",
      statusCode: 301,
    },
    {
      source: "/docs/itemGroups_yml",
      destination: "/config/itemGroups.yml",
      statusCode: 301,
    },
    {
      source: "/docs/language_yml",
      destination: "/config/language.yml",
      statusCode: 301,
    },
    {
      source: "/docs/relation_yml",
      destination: "/config/relation.yml",
      statusCode: 301,
    },
    {
      source: "/docs/repairCosts_yml",
      destination: "/config/repairCosts.yml",
      statusCode: 301,
    },
    {
      source: "/docs/repairing_yml",
      destination: "/config/repairing.yml",
      statusCode: 301,
    },
    {
      source: "/docs/socketGems_yml",
      destination: "/config/socketGems.yml",
      statusCode: 301,
    },
    {
      source: "/docs/socketing_yml",
      destination: "/config/socketing.yml",
      statusCode: 301,
    },
    {
      source: "/docs/tier_yml",
      destination: "/config/tiers-directory",
      statusCode: 301,
    },
  ],
});

#!/bin/bash
if [[ $VERCEL_GIT_COMMIT_REF == "main"  ]] ; then
  cd website
  yarn build
else
  cd docs
  pnpm run build
fi

#!/bin/bash
if [[ $VERCEL_GIT_COMMIT_REF == "9.x"  ]] ; then
  cd docs
  pnpm run build
else
  cd website
  yarn build
fi

import { hasKey } from "@/utils/common";

export function getChangedPropInArray<T>(
  obj: T[],
  oldObj: T[]
): (any | 0 | undefined) {
  // Return property of the object that changed
  let changedProp = undefined;
  let changedIndex = 0;
  obj.filter((p: T, idx: number) => {
    [changedProp, changedIndex] = getChangedPropInObject<T>(p, oldObj[idx], idx);
    return changedProp;
  });
  // console.log(changedProp);
  // console.log(changedIndex);
  return [changedProp, changedIndex];
}

export function getChangedPropInObject<T>(
  obj: T,
  oldObj: T,
  index : number = 0
): (any | 0 | undefined) {
  let changedProp = undefined;
  Object.keys(obj).some((prop: string) => {
    console.info(prop)
    if (oldObj) {
      if (hasKey(obj, prop)) {
        if (typeof obj[prop] === "object") {
          // depth 3
          // @ts-ignore
          if (obj[prop] && Object.keys(obj[prop]).length > 0) {
            // @ts-ignore
            return Object.keys(obj[prop]).some((prop2: string) => {
              const diff =
                // @ts-ignore
                obj[prop][prop2] !== oldObj[prop][prop2];
              if (diff) {
                changedProp = `this.${prop}.${prop2}`;
                // @ts-ignore
                oldObj[prop][prop2] = obj[prop][prop2];
              }
              return diff;
            });
          }
        } else {
          // depth 2
          const diff = obj[prop] !== oldObj[prop];
          if (diff) {
            changedProp = `this.${prop}`;
            // @ts-ignore
            oldObj[prop] = obj[prop];
          }
          return diff;
        }
      }
    }
  });
  console.log(changedProp);
  return [changedProp, index];
}

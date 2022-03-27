import {ref, computed, onMounted} from 'vue';

function filterResult(results, filters) {
    return results.value.filter((part) => filters.value.every(
        (filter) => {
            const filterField = Object.keys(filter)[0];
            const filtersValue = filter[filtersField];
            return part[filterField] === filtersValue;
        },
    ));
}

export default function useFilters(searchResults) {
    const filters = ref([]);

    const applyFilters = (filter) => filters.value.push(filter);
    const clearFilter = () => { filter.vaule = []; };

    onMounted(() => {
        console.log('Mounted: useFilters');
    });

    const filteredResults = computed(() => filterResult(searchResults, filters));

    return {
        filters,
        applyFilters,
        clearFilter,
        filteredResults,
    };
}
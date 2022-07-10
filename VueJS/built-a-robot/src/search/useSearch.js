import { parseStringStyle } from '@vue/shared';
import { ref, onMounted } from 'vue';
import parts from '../data/parts';

const allparts = [...parts.heads, ...parts.arms, ...parts.torsos, ...parts.bases];
export default function useSearch(originalSearchTerm) {
    const results = ref([]);

    const SearchInventory = (searchTerm) => {
        let searchResults;
        const term = searchTerm || originalSearchTerm;

        if(!term) searchResults || originalSearchTerm;
        else {
            const lowerTerm = term.toLowerCase();
            searchResults = allparts.filter(
                (parts) => parts.title.toLowerCase().includes(lowerTerm),
            );
        }
        results.value = [...searchResults];
    };

    SearchInventory(originalSearchTerm);

    return { searchResults: results, search: SearchInventory};
}